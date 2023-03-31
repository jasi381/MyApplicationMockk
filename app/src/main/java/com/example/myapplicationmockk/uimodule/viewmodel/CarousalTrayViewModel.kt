package com.viewlift.uimodule.viewmodel

import android.content.pm.ActivityInfo
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.viewlift.common.label.*
import com.viewlift.common.utils.CommonUtils
import com.viewlift.core.base.BaseViewModel
import com.viewlift.core.data.AppDataRepository
import com.viewlift.core.navigation.NavigationManager
import com.viewlift.core.utils.EventBus
import com.viewlift.network.PageQuery
import com.viewlift.network.data.remote.model.request.ScheduleListParams
import com.viewlift.network.data.remote.model.request.VideoDetailsParams
import com.viewlift.network.data.remote.model.response.*
import com.viewlift.network.data.utils.Utils
import com.viewlift.network.domain.repository.BootstrapRepository
import com.viewlift.uimodule.R
import com.viewlift.uimodule.data.Module
import com.viewlift.uimodule.data.ScheduleEvent
import com.viewlift.uimodule.events.RefreshVideoEvent
import com.viewlift.uimodule.intent.RefreshVideoDataIntent
import com.viewlift.uimodule.schedule.model.CustomCalenderDay
import com.viewlift.uimodule.schedule.model.GameDetails
import com.viewlift.uimodule.uistate.RefreshUiState
import com.viewlift.uimodule.usecase.GameUseCase
import com.viewlift.uimodule.usecase.ScheduleUseCase
import com.viewlift.uimodule.usecase.VideoRefreshUseCase
import com.viewlift.uimodule.utils.UiUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CarousalTrayViewModel @Inject constructor(
    val bootstrapRepository: BootstrapRepository,
    var navigationManager: NavigationManager,
    savedStateHandle: SavedStateHandle,
    private val videoRefreshUseCase: VideoRefreshUseCase,
    private val scheduleUseCase: ScheduleUseCase,
    private val gameUseCase: GameUseCase,
    val appDataRepository: AppDataRepository,
    socialLoginUiState: RefreshUiState
) : BaseViewModel<RefreshUiState, RefreshUiState.RefreshUiStatePartialState, RefreshVideoEvent, RefreshVideoDataIntent>(
    savedStateHandle,
    socialLoginUiState
) {

    var listOfSchedules: List<GameDetails> = listOf()
    var currentPage: Int? = null
    val gameDetails: MutableStateFlow<GameDetailResponse?> = MutableStateFlow(null)
    val videoId: MutableStateFlow<String?> = MutableStateFlow(null)

    var currentVideo: CarouselVideoPlay? = null

    val showImageOrVideo: MutableStateFlow<CarouselVideoPlay?> = MutableStateFlow(null)

    val listOfGames: MutableStateFlow<List<GameDetails>?> = MutableStateFlow(null)

    var placeholderImage: String = ""

    var events: MutableStateFlow<Long?> = MutableStateFlow(null)

    var screenWidth: Int = 0
    var calculatedHeight: Int? = 0

    init {
        viewModelScope.launch {
            placeholderImage =
                bootstrapRepository.getCachedBootstrap()?.appcmsMain?.images?.placeholderCover ?: ""

            EventBus.subscribe<SSEEvent> { sseEvent ->
                Log.e("SSE sseEvent", "${sseEvent} ")
                val gson = Gson()
                var payLoadData: PayLoadData? = null
                try {
                    payLoadData = gson.fromJson(Utils.decodeToBase64(sseEvent.payload), PayLoadData::class.java)

                } catch (ex: Exception){
                    ex.printStackTrace()
                }

                if (payLoadData?.notificationType != null && payLoadData.notificationType.equals("GAME_LIVE_SCORE")) {
                    val gameData = gson.fromJson(payLoadData.payload, GameData::class.java)
                    if (gameData != null) {
                        val status = "${gameData.number}${CommonUtils.getNumberSuffix(gameData.number)} ${gameData.type}"
                        setGameScore(
                            HomeAwayTeamScore(
                                homeTeamScore = gameData.homePoint,
                                awayTeamScore = gameData.awayPoint,
                                gameId = payLoadData.contentMeta?.id,
                                status = status
                            )
                        )
                        val currentGame = getGameState(payLoadData.contentMeta?.id)
                        currentGame?.gameState = "live"
                        if (currentGame != null) {
                            setGameStateChange(currentGame)
                        }
//                    val pair: Pair<GameData, String?> = Pair(gameData, payLoadData.contentMeta?.id)
                        events.value = System.currentTimeMillis()
                    }
                } else if (payLoadData?.notificationType != null && payLoadData.notificationType.equals("GAME_STATE_CHANGE")) {
                    val gameStateChange = gson.fromJson(payLoadData.payload, GameStateChangeInfo::class.java)
                    if (gameStateChange != null) {
                        val gameState = GameChangeState(
                            default = gameStateChange.states?.default?.startDateTime ?: "",
                            pre = gameStateChange.states?.pre?.startDateTime ?: "",
                            post = gameStateChange.states?.post?.startDateTime ?: "",
                            live = gameStateChange.states?.live?.startDateTime ?: "",
                            end = gameStateChange.states?.end?.endDateTime ?: "",
                            gameId = payLoadData.contentMeta?.id ?: "",
                            gameState = gameStateChange.currentState ?: ""
                        )
                        setGameStateChange(gameState)
                        events.value = System.currentTimeMillis()
                    }
                }
            }

            // Carousel Colors
            bootstrapRepository.getCachedBrand()?.general?.backgroundColor?.let { //#071331
                BootstrapColors.generalBackground = it
            }

            bootstrapRepository.getCachedBrand()?.general?.textColor?.let { //#071331
                BootstrapColors.generalTextColor = it
            }

            bootstrapRepository.getCachedBrand()?.cta?.secondary?.styleAttributesWithBorderParts?.backgroundColor?.let {
                BootstrapColors.secondayBackground = it
            }

            bootstrapRepository.getCachedBrand()?.cta?.primary?.styleAttributesWithBorderParts?.backgroundColor?.let {
                BootstrapColors.ctaBGColor = it
            }

            bootstrapRepository.getCachedBrand()?.footer?.link?.styleAttributesParts?.textColor?.let {
                BootstrapColors.footerLink = it
            }

            bootstrapRepository.getCachedBrand()?.footer?.linkActive?.styleAttributesParts?.textColor?.let {
                BootstrapColors.footerLinkActive = it
            }
        }
    }

    fun getAuthToken(){
        appDataRepository.getAuth()
    }

    private var listOfGameStateChange: MutableList<GameChangeState> = mutableListOf()

    fun setGameStateChange(gameChangeState: GameChangeState) {
        viewModelScope.launch {
            val existingGame = listOfGameStateChange.find { it.gameId == gameChangeState.gameId }
            if (existingGame != null) {
                // Game Exists, Update new score
                val index = listOfGameStateChange.indexOf(existingGame)
                listOfGameStateChange[index] = gameChangeState
            } else {
                listOfGameStateChange.add(gameChangeState)
            }

            if (showImageOrVideo.value?.id != gameChangeState.gameId){
                timer?.cancel()
                showImageOrVideo.emit(CarouselVideoPlay(gameChangeState.gameId, true))
            }
        }
    }

    fun getGameState(gameId: String?): GameChangeState? {
        return listOfGameStateChange.find { it.gameId == gameId }
    }

    private var listOfLiveGames: MutableList<HomeAwayTeamScore> = mutableListOf()

    fun setGameScore(gameData: HomeAwayTeamScore) {
        val existingGame = listOfLiveGames.find { it.gameId == gameData.gameId }
        if (existingGame != null) {
            // Game Exists, Update new score
            val index = listOfLiveGames.indexOf(existingGame)
            listOfLiveGames[index] = gameData
        } else {
            listOfLiveGames.add(gameData)
        }
    }

    fun getCustomGameState(gameChangeState: GameChangeState?, gameDefaultInMillis: Long): String {
        val currentTimeInMillis = System.currentTimeMillis()

        val gameScore = listOfLiveGames.find { it.gameId == gameChangeState?.gameId }

        val differenceInMillis = gameDefaultInMillis - currentTimeInMillis

        val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(differenceInMillis)

        if (diffInDays == 0L && (currentTimeInMillis < gameDefaultInMillis)) {
            // Current Day

            return getGameState(gameChangeState, gameDefaultInMillis, gameScore)
        } else {
            // Above 24 hour
            return if(currentTimeInMillis < gameDefaultInMillis){
                // Future Time
                if (gameChangeState != null && gameChangeState.gameState != "post" && gameChangeState.gameState != "end"){
                    UiUtils.getGameStartOnlyDate(gameChangeState.live?.toLong() ?: 0L)
                } else {
                    return getGameState(gameChangeState, gameDefaultInMillis, gameScore)
                }
            } else {
                // Past Time
                return getGameState(gameChangeState, gameDefaultInMillis, gameScore)
            }
        }
    }

    private fun getGameState(
        gameChangeState: GameChangeState?,
        gameDefaultInMillis: Long,
        gameScore: HomeAwayTeamScore?
    ): String {
        if (gameChangeState?.gameState == "default") {
            // Pre State

            if (gameDefaultInMillis != 0L) {
                return "${CarouselLabels.gameStartsAt} ${UiUtils.getPreNoGameText(
                    gameDefaultInMillis
                )}"
            } else {
                return ""
            }

        } else if (gameChangeState?.gameState == "pre" ){
            return if (gameChangeState.live!=null){
                "${CarouselLabels.gameStartsAt} ${UiUtils.getPreNoGameText(gameChangeState.live!!.toLong())}"
            } else {
                ""
            }

        }  else if (gameChangeState?.gameState == "live") {
            // Live State
            return gameScore?.status ?: ""
        } else if (gameChangeState?.gameState == "post") {
            // Post State
            return gameScore?.status ?: CarouselLabels.final
        } else {
            // end State
            return ""
        }
    }

    fun getGameScore(gameId: String?): HomeAwayTeamScore? {
        return listOfLiveGames.find { it.gameId == gameId }
    }

    fun initScheduleModule(item: Module) {
        ScheduleModuleColors.calandarBgColor =  item.layout?.settings?.calandarBgColor ?: ScheduleModuleColors.calandarBgColor
        ScheduleModuleColors.defaultGameColor = item.layout?.settings?.defaultGameColor ?: ScheduleModuleColors.defaultGameColor
        ScheduleModuleColors.liveGameColor = item.layout?.settings?.liveGameColor ?: ScheduleModuleColors.liveGameColor
        ScheduleModuleColors.moduleBackgroundColor = item.layout?.settings?.moduleBackgroundColor ?: ScheduleModuleColors.moduleBackgroundColor
        ScheduleModuleColors.postGameColor = item.layout?.settings?.postGameColor ?: ScheduleModuleColors.postGameColor
        ScheduleModuleColors.preGameColor = item.layout?.settings?.preGameColor ?: ScheduleModuleColors.preGameColor

        item.metadataMap?.let { data->
            ScheduleModuleLabels.fullScheduleText = if(data[ScheduleModuleKeys.fullScheduleText]?.isEmpty() == false) data[ScheduleModuleKeys.fullScheduleText].toString() else ScheduleModuleLabels.fullScheduleText
            ScheduleModuleLabels.goToGameCenterText = if(data[ScheduleModuleKeys.goToGameCenterText]?.isEmpty() == false) data[ScheduleModuleKeys.goToGameCenterText].toString() else ScheduleModuleLabels.goToGameCenterText
            ScheduleModuleLabels.liveScheduleTitle = if(data[ScheduleModuleKeys.liveScheduleTitle]?.isEmpty() == false) data[ScheduleModuleKeys.liveScheduleTitle].toString() else ScheduleModuleLabels.liveScheduleTitle
            ScheduleModuleLabels.postScheduleTitle = if(data[ScheduleModuleKeys.postScheduleTitle]?.isEmpty() == false) data[ScheduleModuleKeys.postScheduleTitle].toString() else ScheduleModuleLabels.postScheduleTitle
        }
    }

    fun getVideoDetails(url: VideoDetailsParams): Flow<RefreshUiState.RefreshUiStatePartialState> =
        flow {
            videoRefreshUseCase(url).onStart {
                emit(RefreshUiState.RefreshUiStatePartialState.Loading)
            }.collect { result ->
                result.onSuccess {
                    emit(RefreshUiState.RefreshUiStatePartialState.Fetched(it))
                }
                result.onFailure { error ->
                    emit(RefreshUiState.RefreshUiStatePartialState.Error(error))
                    Timber.d("Error - > " + error.message)
                    Timber.d(error.stackTraceToString())
                }
            }
        }

    fun getScheduleList(scheduleListParams: ScheduleListParams): Flow<RefreshUiState.RefreshUiStatePartialState> =
        flow {
            scheduleUseCase(scheduleListParams).onStart {
                emit(RefreshUiState.RefreshUiStatePartialState.Loading)
            }.collect { result ->
                result.onSuccess {
                    emit(RefreshUiState.RefreshUiStatePartialState.FetchedScheduleList(it))
                }
                result.onFailure { error ->
                    emit(RefreshUiState.RefreshUiStatePartialState.Error(error))
                    Timber.d("Error - > " + error.message)
                    Timber.d(error.stackTraceToString())
                }
            }
        }


    override fun mapIntents(intent: RefreshVideoDataIntent): Flow<RefreshUiState.RefreshUiStatePartialState> =
        when (intent) {
            is RefreshVideoDataIntent.GetVideoRefreshContent -> getVideoDetails(intent.videoDetailsParams)
            is RefreshVideoDataIntent.GetScheduleList -> getScheduleList(intent.scheduleListParams)
            is RefreshVideoDataIntent.GetGameDetailsContent -> getGameVideo(intent.gameId)
        }

    override fun reduceUiState(
        previousState: RefreshUiState,
        partialState: RefreshUiState.RefreshUiStatePartialState
    ): RefreshUiState = when (partialState) {
        is RefreshUiState.RefreshUiStatePartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false
        )
        is RefreshUiState.RefreshUiStatePartialState.Fetched -> previousState.copy(
            isLoading = false,
            refreshUIstate = partialState.refreshUIstate,
            isError = false
        )
        is RefreshUiState.RefreshUiStatePartialState.FetchedGameDetails -> previousState.copy(
            isLoading = false,
            gameDetailsResponse = partialState.gameDetailsResponse,
            isError = false
        )
        is RefreshUiState.RefreshUiStatePartialState.FetchedScheduleList -> previousState.copy(
            isLoading = false,
            scheduleList = partialState.scheduleListParams,
            isError = false
        )
        is RefreshUiState.RefreshUiStatePartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true,
            error = partialState.throwable,
            apolloApiError = CommonUtils.getApolloError(partialState.throwable)
        )
    }

    fun handlePageQuery(item: Module) {

        val metadataMap = item.metadataMap

        if (metadataMap != null && metadataMap is LinkedHashMap<*, *>) {
            val metadata = metadataMap as LinkedHashMap<String, String>
            CarouselLabels.Quarter =
                if (!metadata[CarouselKeys.Quarter].isNullOrEmpty()) metadata[CarouselKeys.Quarter]!! else CarouselLabels.Quarter
            CarouselLabels.final =
                if (!metadata[CarouselKeys.final].isNullOrEmpty()) metadata[CarouselKeys.final]!! else CarouselLabels.final
            CarouselLabels.gamePreview =
                if (!metadata[CarouselKeys.gamePreview].isNullOrEmpty()) metadata[CarouselKeys.gamePreview]!! else CarouselLabels.gamePreview
            CarouselLabels.gameStartsAt =
                if (!metadata[CarouselKeys.gameStartsAt].isNullOrEmpty()) metadata[CarouselKeys.gameStartsAt]!! else CarouselLabels.gameStartsAt
            CarouselLabels.live =
                if (!metadata[CarouselKeys.live].isNullOrEmpty()) metadata[CarouselKeys.live]!! else CarouselLabels.live
            CarouselLabels.period =
                if (!metadata[CarouselKeys.period].isNullOrEmpty()) metadata[CarouselKeys.period]!! else CarouselLabels.period
            CarouselLabels.postGame =
                if (!metadata[CarouselKeys.postGame].isNullOrEmpty()) metadata[CarouselKeys.postGame]!! else CarouselLabels.postGame
            CarouselLabels.poweredBy =
                if (!metadata[CarouselKeys.poweredBy].isNullOrEmpty()) metadata[CarouselKeys.poweredBy]!! else CarouselLabels.poweredBy
            CarouselLabels.preGame =
                if (!metadata[CarouselKeys.preGame].isNullOrEmpty()) metadata[CarouselKeys.preGame]!! else CarouselLabels.preGame
            CarouselLabels.readArticle =
                if (!metadata[CarouselKeys.readArticle].isNullOrEmpty()) metadata[CarouselKeys.readArticle]!! else CarouselLabels.readArticle
            CarouselLabels.seeMoreTxt =
                if (!metadata[CarouselKeys.seeMoreTxt].isNullOrEmpty()) metadata[CarouselKeys.seeMoreTxt]!! else CarouselLabels.seeMoreTxt
            CarouselLabels.watchNowInGameCenter =
                if (!metadata[CarouselKeys.watchNowInGameCenter].isNullOrEmpty()) metadata[CarouselKeys.watchNowInGameCenter]!! else CarouselLabels.watchNowInGameCenter
        }
    }

    var timer: CountDownTimer? = null

    fun startTimer(videoOrGameId: String?) {
        Timber.d("CarouselTray CarousalItem 07 Timer start ${videoOrGameId}")


        timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.d("CarouselTray CarousalItem 07 Timer started ${videoOrGameId} second ${millisUntilFinished}")

            }

            override fun onFinish() {
                Timber.d("CarouselTray CarousalItem 07 Timer ended ${videoOrGameId} second ${3000}")
                viewModelScope.launch {
                    showImageOrVideo.emit(CarouselVideoPlay(videoOrGameId, true))
                }
                timer?.cancel()
            }
        }
        timer?.start()
    }

    fun resetCountdown() {
        viewModelScope.launch {
            showImageOrVideo.emit(CarouselVideoPlay("", false))
        }
    }

    fun loadVideo(currentPageId: String?) {
        startTimer(currentPageId)
    }



    fun enterFullScreen() {
//        viewModelScope.launch {
//            if(hideShowBottombar != EventBus.AppEvent.HIDE_BOTTOM_BAR){
//                EventBus.invokeEvent(EventBus.AppEvent.HIDE_BOTTOM_BAR)
//                hideShowBottombar = EventBus.AppEvent.HIDE_BOTTOM_BAR
//            }
//            if(hideShowTopbar != EventBus.AppEvent.HIDE_TOP_BAR){
//                EventBus.invokeEvent(EventBus.AppEvent.HIDE_TOP_BAR)
//                hideShowTopbar = EventBus.AppEvent.HIDE_TOP_BAR
//            }
//        }
    }

    fun exitFullScreen() {
//        viewModelScope.launch {
//            if(hideShowBottombar != EventBus.AppEvent.SHOW_BOTTOM_BAR){
//                EventBus.invokeEvent(EventBus.AppEvent.SHOW_BOTTOM_BAR)
//                hideShowBottombar = EventBus.AppEvent.SHOW_BOTTOM_BAR
//            }
//            if(hideShowTopbar != EventBus.AppEvent.SHOW_TOP_BAR){
//                EventBus.invokeEvent(EventBus.AppEvent.SHOW_TOP_BAR)
//                hideShowTopbar = EventBus.AppEvent.SHOW_TOP_BAR
//            }
//        }
    }

    fun updateScheduleEvents(selectedDate: CustomCalenderDay) {
        val day: Int = selectedDate.localDate.dayOfMonth
        val month: Int = selectedDate.localDate.month.value - 1
        val year: Int = selectedDate.localDate.year

        val startCalendar = Calendar.getInstance()
        startCalendar.set(year, month, day)
        val startTime = startCalendar.timeInMillis / 1000

        startCalendar.add(Calendar.DAY_OF_MONTH, 1)

        val endTime = startCalendar.timeInMillis / 1000

        acceptIntent(
            RefreshVideoDataIntent.GetScheduleList(
                ScheduleListParams(
                    url = "https://api.develop.monumentalsportsnetwork.com/v3/content/game/schedule",
                    site = "msndev",
                    start = startTime,
                    end = null,
                    offset = 0,
                    limit = 100
                )
            )
        )
    }

    fun setListOfGames(listOfSchedules: List<GameDetails>) {
        listOfGames.value = listOfSchedules
    }

    fun getGameVideo(gameId: String): Flow<RefreshUiState.RefreshUiStatePartialState>  = flow {
        val baseUrl = bootstrapRepository.getCachedMain()?.apiBaseUrl
        val site = bootstrapRepository.getCachedMain()?.internalName
        val gameUrl: String = "$baseUrl/v3/content/game/$gameId?site=$site"

        gameUseCase(gameUrl).onStart {
//                emit(RefreshUiState.RefreshUiStatePartialState.Loading)
        }.collect { result ->
            result.onSuccess {
                Timber.d("data ${it}")
                gameDetails.value = it
            }
            result.onFailure { error ->
//                    emit(RefreshUiState.RefreshUiStatePartialState.Error(error))
                Timber.d("Error - > " + error.message)
                Timber.d(error.stackTraceToString())
            }
        }
    }

    fun playGameVideo(gameId: String?) {
        viewModelScope.launch {
//            val apiUrl = "${bootstrapRepository.getCachedBootstrap()?.appcmsMain?.apiBaseUrl}/v3/content/game/${gameId}"
//            val internalName = bootstrapRepository.getCachedBootstrap()?.appcmsMain?.internalName
//
//            val gameDetailsParams = GameDetailsParams(
//                url = apiUrl,
//                site = internalName
//            )

            acceptIntent(RefreshVideoDataIntent.GetGameDetailsContent(gameId!!))
        }
    }

    fun toggleFullScreenPlayer(orientation : Int){
        viewModelScope.launch {
            if(orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
                EventBus.invokeEvent(EventBus.AppEvent.HIDE_HEADER_FOOTER)
            } else { // portrait
                EventBus.invokeEvent(EventBus.AppEvent.SHOW_HEADER_FOOTER)
            }
        }
    }
}

data class CarouselVideoPlay(
    var id: String? = "",
    var playVideo: Boolean = false
)

data class GameChangeState(
    var default: String? = "",
    var pre: String? = "",
    var post: String? = "",
    var live: String? = "",
    var end: String? = "",
    var gameId: String? = "",
    var gameState: String? = ""
)

data class HomeAwayTeamScore(
    var homeTeamScore: Int? = null,
    var awayTeamScore: Int? = null,
    var gameId: String? = "",
    var status: String? = ""
)