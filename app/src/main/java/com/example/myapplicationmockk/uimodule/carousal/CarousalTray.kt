package com.viewlift.uimodule.carousal

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.common.label.BootstrapColors
import com.viewlift.common.label.CarouselLabels
import com.viewlift.common.label.ScheduleModuleColors
import com.viewlift.common.ui.composable.HLSVideoPlayer
import com.viewlift.common.ui.composable.gradientBottom
import com.viewlift.common.utils.parse
import com.viewlift.core.extensions.collectAsStateWithLifecycle
import com.viewlift.core.utils.Utils
import com.viewlift.network.PageQuery
import com.viewlift.network.data.remote.model.request.VideoDetailsParams
import com.viewlift.network.data.remote.model.response.GameDetailResponse
import com.viewlift.network.fragment.PartialGame
import com.viewlift.uimodule.data.*
import com.viewlift.uimodule.intent.RefreshVideoDataIntent
import com.viewlift.uimodule.tray.util.TrayUtil
import com.viewlift.uimodule.utils.UiUtils
import com.viewlift.uimodule.videoPlayer.VideoPlayerView
import com.viewlift.uimodule.viewmodel.CarousalTrayViewModel
import com.viewlift.uimodule.viewmodel.GameChangeState
import timber.log.Timber

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarouselTray(
    item: Module,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: CarousalTrayViewModel = hiltViewModel()) {

    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.currentPage = null
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    viewModel.handlePageQuery(item)
    Column(
        Modifier
            .fillMaxWidth()
            .background(BootstrapColors.generalBackground.parse)
    ) {
        val pagerState: PagerState = rememberPagerState()

        Box(modifier = Modifier
            .fillMaxWidth()
            .background(ScheduleModuleColors.moduleBackgroundColor.parse)){
            HorizontalPager(
                count = item.contentData?.size ?: 0,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth(),
            ) { index ->

                if (pagerState.currentPage == index) {
                    viewModel.resetCountdown()
                    Timber.d("CarouselTray CarousalItem 01")
                    CarousalItem(item?.contentData?.get(index), settings = item.layout?.settings, item.layout!!)
//                val trayLayout: Layout = UiUtils.convertPageQueryToFontSettings(item.onCuratedTrayModule?.layout)

//                CarousalItem(item.onCuratedTrayModule?.contentData?.get(index), settings = settings, trayLayout = trayLayout)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            activeColor = BootstrapColors.footerLinkActive.parse,
            indicatorShape = RectangleShape,
            indicatorWidth = 18.dp,
            indicatorHeight = 2.dp,
            spacing = 6.dp,
            inactiveColor = BootstrapColors.footerLink.parse
        )

        if (item.contentData?.get(pagerState.currentPage) != null) {
            if (viewModel.currentPage != pagerState.currentPage){
                viewModel.currentPage = pagerState.currentPage

                // preview - default
                // pre preview, live,
                // post Video

                val currentItem = item.contentData.get(pagerState.currentPage)
                val currentState = currentItem.currentState

                val gameId = if (currentState == "default"){
                    currentItem.preview?.id
                } else if (currentState == "pre" || currentState == "live" || currentState == "post" ){
                    currentItem.livestreams.first().id
                } else {
                    currentItem.highlights.first().id
                }

                viewModel.loadVideo(gameId)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
fun CarousalItem(
    contentData: ContentData?,
    settings: Settings?,
    trayLayout: Layout,
    viewModel: CarousalTrayViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier

    ) {
        Timber.d("CarouselTray CarousalItem 02")
        CarouselImageVideoData(contentData, settings)

        val modifier = Modifier.defaultMinSize(120.dp)

        if (contentData?.contentType == "GAME"){
            CarouselLiveScore(modifier = modifier, contentData = contentData, trayLayout = trayLayout)
        } else if (contentData?.contentType == "VIDEO"){
            CarouselTitle(contentData.gist?.title, modifier)
        } else if (contentData?.contentType == "ARTICLE"){
            CarouselTitle(contentData.gist?.title, modifier)
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
            shape = RoundedCornerShape(3.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = BootstrapColors.ctaBGColor.parse),

            ) {

            var buttonIcon: Int = com.viewlift.common.R.drawable.ic_article
            var buttonText = CarouselLabels.readArticle

            if (contentData?.contentType == "GAME"){
                buttonIcon = com.viewlift.common.R.drawable.ic_play
                buttonText = CarouselLabels.watchNowInGameCenter
            } else if (contentData?.contentType == "VIDEO"){
                buttonIcon = com.viewlift.common.R.drawable.ic_play
                buttonText = "Watch Now"
            } else if (contentData?.contentType == "ARTICLE"){
                buttonIcon = com.viewlift.common.R.drawable.ic_article
                buttonText = CarouselLabels.readArticle
            }

            Image(
                painter = painterResource(id = buttonIcon),
                contentDescription = null,
                modifier = Modifier.size(10.dp)
            )

            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

            Text(
                text = buttonText,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CarouselImageVideoData(
    contentData: ContentData?,
    settings: Settings?,
    viewModel: CarousalTrayViewModel = hiltViewModel()
) {
    Timber.d("CarouselTray CarousalItem 03")
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current


    viewModel.screenWidth = configuration.screenWidthDp

    viewModel.calculatedHeight =
        TrayUtil.getCalculatedHeight(settings?.thumbnailType, viewModel.screenWidth)

    when(configuration.orientation){
                Configuration.ORIENTATION_LANDSCAPE -> {
                        viewModel.screenWidth = with(density) {configuration.screenWidthDp.dp.roundToPx()}//configuration.screenWidthDp
                        viewModel.calculatedHeight = with(density) {configuration.screenHeightDp.dp.roundToPx()}//viewModel.screenWidth * 9/16
                              //  configuration.screenHeightDp  //with(density) {configuration.screenHeightDp.dp.roundToPx()}//configuration.screenHeightDp + 30
                    } else -> {
                    viewModel.screenWidth = configuration.screenWidthDp
                    viewModel.calculatedHeight = viewModel.screenWidth * 9/16
                }
            }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.78F, false)
            //.height(viewModel.calculatedHeight?.dp ?: 221.dp)
            //.width(viewModel.screenWidth.dp)
    ) {

        if (contentData?.contentType == "GAME"){
            Timber.d("CarouselTray CarousalItem 04 pre")

            CarouselGameScreen(contentData, settings, this)
        } else if (contentData?.contentType == "VIDEO"){
            CarouselVideoScreen(contentData, settings, this)
        } else if (contentData?.contentType == "ARTICLE"){
            val imageUrl = TrayUtil.getOrientationType(
                contentData.gist?.imageGist,
                settings?.thumbnailType
            )

            CarouselImage(
                imageUrl ?: viewModel.placeholderImage,
                viewModel.screenWidth,
                viewModel.calculatedHeight
            )
        }
    }
}

@Composable
fun CarouselVideoScreen(
    contentData: ContentData?,
    settings: Settings?,
    boxScope: BoxScope,
    viewModel: CarousalTrayViewModel = hiltViewModel()
) {

    val showImageOrVideo by viewModel.showImageOrVideo.collectAsStateWithLifecycle(null)

    boxScope.apply {
        Timber.d("CarouselTray CarousalItem 04")
        if(showImageOrVideo!=null && !showImageOrVideo!!.id.isNullOrEmpty()){
            if (showImageOrVideo!!.id == contentData?.id) {
                val imageUrl = TrayUtil.getOrientationType(
                    contentData?.gist?.imageGist,
                    settings?.thumbnailType
                )

                if (showImageOrVideo!!.playVideo) {
                    CarouselVideo(viewModel.screenWidth, viewModel.calculatedHeight, imageUrl)
                } else {


                    CarouselImage(
                        imageUrl ?: viewModel.placeholderImage,
                        viewModel.screenWidth,
                        viewModel.calculatedHeight
                    )
                }
            }
        } else {
            val image = TrayUtil.getOrientationType(
                contentData?.gist?.imageGist,
                settings?.thumbnailType
            )

                    CarouselImage(
                        image ?: viewModel.placeholderImage,
                        viewModel.screenWidth,
                        viewModel.calculatedHeight
                    )
                }
        }
}

@Composable
fun CarouselGameScreen(
    contentData: ContentData?,
    settings: Settings?,
    boxScope: BoxScope,
    viewModel: CarousalTrayViewModel = hiltViewModel()
) {

    val showImageOrVideo by viewModel.showImageOrVideo.collectAsStateWithLifecycle(initialValue = null)

    boxScope.apply {
        Timber.d("CarouselTray CarousalItem 04")
        if(showImageOrVideo!=null){
            Timber.d("CarouselTray CarousalItem 05 ${showImageOrVideo!!.id} contentData ${contentData?.id}")
            if (showImageOrVideo!!.id == contentData?.id) {
                if (showImageOrVideo!!.playVideo) {
                    viewModel.currentVideo = showImageOrVideo
                    viewModel.playGameVideo(contentData?.id)
                    val imageUrl = TrayUtil.getOrientationType(
                        contentData?.gist?.imageGist,
                        settings?.thumbnailType
                    )
                    CarouselGame(viewModel.screenWidth, viewModel.calculatedHeight, imageUrl)


                } else {
                    val imageUrl = TrayUtil.getOrientationType(
                        contentData?.gist?.imageGist,
                        settings?.thumbnailType
                    )

                    CarouselImage(
                        imageUrl ?: viewModel.placeholderImage,
                        viewModel.screenWidth,
                        viewModel.calculatedHeight
                    )
                }
                GameOverlay(this, contentData)

            } else {
                Timber.d("CarouselTray CarousalItem 06")

                val imageUrl = TrayUtil.getOrientationType(
                    contentData?.gist?.imageGist,
                    settings?.thumbnailType
                )

                CarouselImage(
                    imageUrl ?: viewModel.placeholderImage,
                    viewModel.screenWidth,
                    viewModel.calculatedHeight
                )

                GameOverlay(this, contentData)
            }
        } else {
            Timber.d("CarouselTray CarousalItem 06")

            val imageUrl = TrayUtil.getOrientationType(
                contentData?.gist?.imageGist,
                settings?.thumbnailType
            )

            CarouselImage(
                imageUrl ?: viewModel.placeholderImage,
                viewModel.screenWidth,
                viewModel.calculatedHeight
            )

            GameOverlay(this, contentData)
        }
    }
}

@Composable
private fun GameOverlay(
    boxScope: BoxScope,
    contentData: ContentData?,
    viewModel: CarousalTrayViewModel = hiltViewModel()
) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    Log.e("Game event start", "${events} ")

    val gameState: GameChangeState? = viewModel.getGameState(contentData?.id)

    val gameStartTime: Long = if (events != null){
        gameState?.live?.toLong()?.times(1000L) ?: 0L
    } else {
        if (!contentData?.schedules.isNullOrEmpty()) (contentData?.schedules?.first()?.startDate?.toLong()!! * 1000) else 0L
    }

    var currentGameState = viewModel.getCustomGameState(gameState, gameStartTime)

//    val states = contentData?.states

//    if(currentGameState.isEmpty()){
//        val defaultGameState = GameChangeState(
//            default = states?.default?.startDateTime?.toString() ?: "",
//            pre = states?.pre?.startDateTime?.toString() ?: "",
//            post = states?.post?.startDateTime?.toString() ?: "",
//            live = states?.live?.startDateTime?.toString() ?: "",
//            end = states?.end?.startDateTime?.toString() ?: "",
//            gameId = contentData?.id,
//            gameState = contentData?.currentState ?: ""
//        )
//        viewModel.setGameStateChange(defaultGameState)
//
//        currentGameState = viewModel.getCustomGameState(defaultGameState, contentData?.schedules)
//    }


    boxScope.apply {

        Row(modifier = Modifier.align(Alignment.TopStart)) {
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Spacer(modifier = Modifier.height(15.dp))
                val game = viewModel.getGameState(contentData?.id)
                if (game?.gameState == "default"){
                    GamePreview()

                } else if (game?.gameState == "pre") {
                    // Pre State
                    PreGamePreview()

                } else if (game?.gameState == "live") {
                    // Live State
                    LiveGame()
                } else if (game?.gameState == "post") {
                    // Post State
                    PostGame()
                } else {
                    // end State
                }
            }

        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .gradientBottom(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
                Spacer(modifier = Modifier.width(10.dp))
                val metaData = contentData?.metadata?.find { it?.name == "presentedBy" }

                metaData?.let {
                    Text(
                        text = CarouselLabels.poweredBy,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Italic,
                    )

                    Spacer(modifier = Modifier.width(5.dp))


                    AsyncImage(
                        model = metaData.value,
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(30.dp)
                            .width(60.dp)
                            .clip(
                                RoundedCornerShape(
                                    topEnd = 7.dp,
                                    topStart = 7.dp
                                )
                            )
                    )
                }
            }
            CustomGameState(currentGameState)
        }
    }
}

@Composable
private fun CustomGameState(
    currentState: String
) {
//
//    val currentState = contentData?.onGame?.partialGame?.currentState
//
//    val preTime = contentData?.onGame?.partialGame?.schedules?.first()?.startDate
//
//

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = currentState,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.width(10.dp))

    }
}

@Composable
fun CarouselTitle(title: String?, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        if (!title.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 30.sp,
                maxLines = 2,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun StartTimer(contentData: ContentData, viewModel: CarousalTrayViewModel = hiltViewModel()) {
    val context: Context = LocalContext.current

    val deviceId: String? = Utils.getDeviceId(context)

    val url: String = stringResource(
        id = com.viewlift.network.R.string.app_cms_entitlement_api_url,
        "https://api.develop.monumentalsportsnetwork.com",
        contentData.id ?: "",
        deviceId ?: "",
        "android_phone",
        "android",
        false
    )

    //    //ToDo Replace this once store country code implemented in android
    //    if (isTVPlatform() && storeCountryCode != null) {
    //        url = "$url&store_countryCode=$storeCountryCode"
    //    }

    val timer = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {

            viewModel.acceptIntent(
                RefreshVideoDataIntent.GetVideoRefreshContent(
                    VideoDetailsParams(
                        url = url,
                        id = contentData.id ?: "",
                        deviceId = deviceId,
                        deviceType = "android_phone",
                        contentConsumption = "android",
                        download = false
                    )
                )
            )
        }
    }
    timer.start()
}

@Composable
private fun CarouselImage(
    carouselImage: String,
    width: Int,
    height: Int? = null
) {


    val modifier: Modifier = Modifier.width(width.dp)
    if (height != null) {
        modifier.height(height.dp)
    }

    AsyncImage(
        model = carouselImage,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topEnd = 7.dp,
                    topStart = 7.dp
                )
            )
    )
}


@Composable
private fun CarouselVideo(
    width: Int,
    height: Int? = null,
    imageUrl: String?,
    viewModel: CarousalTrayViewModel = hiltViewModel()
) {

    val videoId by viewModel.videoId.collectAsStateWithLifecycle(null)
    Timber.d("PlayerStarted in Carousel VideoID $videoId")

    val modifier: Modifier = Modifier.width(width.dp)
    if (height != null) {
        modifier.height(height.dp)
    }

    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topEnd = 7.dp,
                    topStart = 7.dp
                )
            )
    ) {
        if(videoId != null){
            VideoPlayerView(videoId!!)

        } else {
            CarouselImage(
                imageUrl ?: viewModel.placeholderImage,
                viewModel.screenWidth,
                viewModel.calculatedHeight
            )
        }
    }
}

@Composable
private fun CarouselGame(
    width: Int,
    height: Int? = null,
    imageUrl: String?,
    viewModel: CarousalTrayViewModel = hiltViewModel()
) {

    val gameDetails: GameDetailResponse? by viewModel.gameDetails.collectAsStateWithLifecycle(null)
    Timber.d("PlayerStarted in Carousel gameId ${gameDetails?.id}")

    val modifier: Modifier = Modifier.width(width.dp)
    if (height != null) {
        modifier.height(height.dp)
    }

    if(gameDetails!=null){
        Box(
            modifier = modifier
                .clip(
                    RoundedCornerShape(
                        topEnd = 7.dp,
                        topStart = 7.dp
                    )
                )
        ) {

            val videoId = UiUtils.getVideoIdFromGameId(gameDetails)

            if(videoId!=null){
                VideoPlayerView(videoId)
            } else {
                CarouselImage(
                    imageUrl ?: viewModel.placeholderImage,
                    viewModel.screenWidth,
                    viewModel.calculatedHeight
                )
            }
        }
    } else {
        CarouselImage(
            imageUrl ?: viewModel.placeholderImage,
            viewModel.screenWidth,
            viewModel.calculatedHeight
        )
    }
}











