package com.viewlift.uimodule.tray.ui.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.viewlift.uimodule.data.ContentData
import com.viewlift.uimodule.data.Settings
import com.viewlift.uimodule.tray.listener.TrayActionListener
import com.viewlift.uimodule.tray.util.TrayType
import com.viewlift.uimodule.tray.util.TrayUtil


@Composable
fun TrayImage(
    data: ContentData,
    trayType: String,
    isRoundCorner: Boolean,
    settings: Settings?,
    listener: TrayActionListener
) {
    val shape = if (isRoundCorner) RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomEnd = 0.dp,
        bottomStart = 0.dp
    ) else RoundedCornerShape(corner = CornerSize(0.dp))


    var placeholder: MemoryCache.Key? = null
    val imageDpSize: DpSize = TrayUtil.trayImageSize(trayType)
    val imageUrl = TrayUtil.trayImage(trayType, data.gist?.imageGist!!, imageDpSize)
    var playIconVisible = if (data.gist.contentType.equals("video", true)) true else false
    val runtimeText = TrayUtil.getRuntime(data.runtime)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            //.clickable { onSnackClick(snack.id) }
            .padding(horizontal = 0.dp)
    ) {
        val (trayThumbImage, trayBadgeImage, trayPlayIcon, trayRuntime) = createRefs()
        //val image = AnimatedImageVector.animatedVectorResource(id = R.drawable.image_loading_placeholder)
        //val atEnd by remember { mutableStateOf(false) }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl).apply {
                    if (TrayUtil.tryType(trayType) == TrayType.newsTray02) {
                        crossfade(true)
                        //placeholder(R.drawable.ic_launcher_foreground)
                        transformations(
                            CircleCropTransformation()       // Circle Crop Transformation
                        )
                    }
                }
                // .parameters(image.parameters)
                .build(),
            placeholder = painterResource(id = TrayUtil.placeholderMap.get(settings?.thumbnailType)!!),
            error = painterResource(id = TrayUtil.placeholderMap.get(settings?.thumbnailType)!!),
            onSuccess = { placeholder = it.result.memoryCacheKey },
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(trayThumbImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .size(imageDpSize)
                .clip(shape)
        )
        if (runtimeText.trim().length > 0 && settings?.showContentDuration == true) {

            Text(
                text = runtimeText,
                style = TextStyle(fontSize = 10.sp),
                color = Color.Black,
                modifier = Modifier
                    .constrainAs(trayRuntime) {
                        start.linkTo(trayThumbImage.start)
                        bottom.linkTo(trayThumbImage.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .padding(16.dp)
                    .background(Color(0x9fffffff), shape = RoundedCornerShape(12.dp))
                    .padding(4.dp)
            )

        }
        TrayBadgeImage(data = data, trayType = trayType, imageDpSize = imageDpSize,
            modifier = Modifier
                .size(imageDpSize)
                .clip(shape)
                .constrainAs(trayBadgeImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )
        if (playIconVisible) {
            val playImageSize = (TrayUtil.trayTextWidth(trayType) / 2)
            PlayImageIcon(data = data,
                imgDpSize = playImageSize,
                listener = listener,
                modifier = Modifier
                    .size(playImageSize)
                    //.clip(shape)
                    .constrainAs(trayPlayIcon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    })
        }
    }
}

@Composable
fun PlayImageIcon(
    data: ContentData,
    imgDpSize: Dp,
    listener: TrayActionListener,
    modifier: Modifier
) {
    Image(
        painter = painterResource(id = TrayUtil.playIconRes),
        contentDescription = data.gist?.title,
        modifier = modifier
            .size(imgDpSize)
            .clickable { listener.playFromTrayAction(data.gist?.title + " + playe Video + " + data.gist?.id) }// TODO :- Need to decide whether to pass content Id or Parma link
    )


}

@Composable
fun TrayBadgeImage(data: ContentData, trayType: String, imageDpSize: DpSize, modifier: Modifier) {
    if (data.gist?.badgeImages != null) {
        var placeholder: MemoryCache.Key? = null
        val imageUrl = TrayUtil.trayImage(trayType, data.gist.badgeImages, imageDpSize)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl).apply {
                    if (TrayUtil.tryType(trayType) == TrayType.newsTray02) {
                        crossfade(true)
                        transformations(
                            CircleCropTransformation()       // Circle Crop Transformation
                        )
                    }
                }
                .build(),
            placeholder = ColorPainter(Color.Transparent),
            error = ColorPainter(Color.Transparent),
            onSuccess = { placeholder = it.result.memoryCacheKey },
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
        )
    }
}

@Composable
fun TrayParallaxImage(imageUrl: String?, resPlaceholder : Int, modifier : Modifier){
    var placeholder: MemoryCache.Key? = null
    val imageDpSize = DpSize(275.dp, 244.dp)
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .build(),
        onSuccess = { placeholder = it.result.memoryCacheKey },
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun TrayRuntimeText(runtime: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .background(Color(0x9fffffff), shape = RoundedCornerShape(20.dp))

            .padding(4.dp)
            //.clip(RoundedCornerShape(20.dp))
    ) {
        Text(
            text = runtime,
            style = TextStyle(fontSize = 12.sp),
            color = Color.Black
        )
    }
}

@Composable
fun VLTray01Image(
    data: ContentData,
    settings: Settings?,
    listener: TrayActionListener
) {
    val shape =  RoundedCornerShape(
        topStart = 4.dp,
        topEnd = 4.dp,
        bottomEnd = 0.dp,
        bottomStart = 0.dp
    )

    // Intentionally not a state object to avoid recomposition.
    var placeholder: MemoryCache.Key? = null
    val trayType = settings?.thumbnailType!!
    val imageDpSize: DpSize = TrayUtil.trayImageSize(trayType)
    val imageUrl = TrayUtil.trayImage(trayType, data.gist?.imageGist!!, imageDpSize)
    var playIconVisible = if (data.gist.contentType.equals("video", true)
        ||data.gist.contentType.equals("VIDEOPLAYLIST",true)) true else false
    val runtimeText = TrayUtil.getRuntime(data.runtime)
    println(" TrayImage runtime = " + data.runtime +" thumbnailType =: "+settings?.thumbnailType)
    println(" TrayImage runtime ==  " + runtimeText)
    //todo below is just dummy for development tobe change later
    //val isPlayList = if (settings.thumbnailType.equals("1*1",false)) true else false
    val isPlayList = if (data.gist?.contentType.equals("Playlist", true)|| data.gist?.contentType?.contains("Playlist", true) ?:false ) true else false

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            //.clickable { onSnackClick(snack.id) }
            .padding(horizontal = 0.dp)
    ) {
        val (trayThumbImage, trayBadgeImage, trayPlayIcon, trayRuntime,progressIndicator, traySlider) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .build(),
            placeholder = painterResource(id = TrayUtil.placeholderMap.get(settings?.thumbnailType)!!),
            error = painterResource(id = TrayUtil.placeholderMap.get(settings?.thumbnailType)!!),
            onSuccess = { placeholder = it.result.memoryCacheKey },
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .constrainAs(trayThumbImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .size(imageDpSize)
                .clip(shape)
        )
        if (!isPlayList && runtimeText.trim().isNotEmpty() && !runtimeText.trim().equals("00",true) && settings?.showContentDuration == true) {
            Text(
                text = runtimeText,
                style = TrayUtil.TypoGraphyMap["trayItemInfo"]!!,
                color = Color.Black,
                modifier = Modifier
                    .constrainAs(trayRuntime) {
                        start.linkTo(trayThumbImage.start)
                        bottom.linkTo(trayThumbImage.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(Color(0x9fffffff), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        TrayBadgeImage(data = data, trayType = trayType, imageDpSize = imageDpSize,
            modifier = Modifier
                .size(imageDpSize)
                .clip(shape)
                .constrainAs(trayBadgeImage) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )

        //TODO : uncomment when continue watching is implemented
        /*LinearProgressIndicator(progress = .5f,
            trackColor =  Color.Blue,
            color=Color.Red,//CF082D
            modifier = Modifier.constrainAs(traySlider){
            start.linkTo(parent.start)
            end.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            height= Dimension.value(5.dp)
            width=Dimension.matchParent
        }
        )*/
        if (playIconVisible) {


            val playImageSize = if(isPlayList) 32.dp else 48.dp
                val modifier = if (isPlayList) {
                    Modifier
                        .size(playImageSize)
                        //.clip(shape)
                        .constrainAs(trayPlayIcon) {
                            start.linkTo(parent.start, 9.dp)
                            bottom.linkTo(parent.bottom, 11.dp)
                        }
                } else {
                    Modifier
                        .size(playImageSize)
                        //.clip(shape)
                        .constrainAs(trayPlayIcon) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                }
            PlayImageIcon(data = data,
                imgDpSize = playImageSize,
                listener = listener,
                modifier = modifier)
        }
    }
}

@Composable
fun TrayImageTopThumb(topItemData : ContentData, imageDpSize: DpSize){

    var placeholder: MemoryCache.Key? = null

    //val topItemData = module.contentData[0]
    val imageUrl = TrayUtil.trayImage("16*9", topItemData.gist?.imageGist!!, imageDpSize)
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .build(),
        placeholder = painterResource(id = TrayUtil.placeholderMap.get("16*9")!!),
        error = painterResource(id = TrayUtil.placeholderMap.get("16*9")!!),
        onSuccess = { placeholder = it.result.memoryCacheKey },
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .size(imageDpSize)
            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
    )
}

@Composable
fun RelatedTrayImageThumb(
    data: ContentData,
    settings: Settings?,
    imageDpSize: DpSize,
    listener: TrayActionListener
    ) {
        val shape =  RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 4.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        )

        // Intentionally not a state object to avoid recomposition.
        var placeholder: MemoryCache.Key? = null
        val trayType = "16*9"
        val imageUrl = TrayUtil.trayImage(trayType, data.gist?.imageGist!!, imageDpSize)
        var playIconVisible = if (data.gist.contentType.equals("video", true)
            ||data.gist.contentType.equals("VIDEOPLAYLIST",true)) true else false
        val runtimeText = TrayUtil.getRuntime(data.runtime)
        val isPlayList = if (data.gist?.contentType.equals("Playlist", true)|| data.gist?.contentType?.contains("Playlist", true) ?:false ) true else false

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                //.clickable { onSnackClick(snack.id) }
                .padding(horizontal = 0.dp)
        ) {
            val (trayThumbImage, trayBadgeImage, trayPlayIcon, trayRuntime,progressIndicator) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                placeholder = painterResource(id = TrayUtil.placeholderMap.get(trayType)!!),
                error = painterResource(id = TrayUtil.placeholderMap.get(trayType)!!),
                onSuccess = { placeholder = it.result.memoryCacheKey },
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .constrainAs(trayThumbImage) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(imageDpSize)
                    .clip(shape)
            )
            if (!isPlayList && runtimeText.trim().isNotEmpty() && !runtimeText.trim().equals("00",true) && settings?.showContentDuration == true) {
                Text(
                    text = runtimeText,
                    style = TrayUtil.TypoGraphyMap["trayItemInfo"]!!,
                    color = Color.Black,
                    modifier = Modifier
                        .constrainAs(trayRuntime) {
                            start.linkTo(trayThumbImage.start)
                            bottom.linkTo(trayThumbImage.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .background(Color(0xBFFFFFFF), shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            TrayBadgeImage(data = data, trayType = trayType, imageDpSize = imageDpSize,
                modifier = Modifier
                    .size(imageDpSize)
                    .clip(shape)
                    .constrainAs(trayBadgeImage) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )

            //TODO : uncomment when continue watching is implemented
            /*LinearProgressIndicator(progress = .5f,
                trackColor =  Color.Blue,
                color=Color.Red,//CF082D
                modifier = Modifier.constrainAs(progressIndicator){
                start.linkTo(parent.start)
                end.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                height= Dimension.value(5.dp)
                width=Dimension.matchParent
            }
            )*/
            if (playIconVisible) {


                val playImageSize = if(isPlayList) 32.dp else 48.dp
                val modifier = Modifier
                        .size(playImageSize)
                        //.clip(shape)
                        .constrainAs(trayPlayIcon) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }

                PlayImageIcon(data = data,
                    imgDpSize = playImageSize,
                    listener = listener,
                    modifier = modifier)
            }
        }
    }