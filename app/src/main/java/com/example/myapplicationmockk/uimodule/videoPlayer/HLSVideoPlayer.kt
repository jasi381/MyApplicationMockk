package com.viewlift.uimodule.videoPlayer

import android.app.Activity
import android.app.MediaRouteButton
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.eventbus.EventBus
import com.viewlift.uimodule.R
import com.viewlift.uimodule.viewmodel.CarousalTrayViewModel
import com.vl.viewlift.playersdk.events.VLOmEventListener
import com.vl.viewlift.playersdk.events.VLPlayerCustomEventListener
import com.vl.viewlift.playersdk.events.VLPlayerFullScreenListener
import com.vl.viewlift.playersdk.events.VLPlayerVideoEventListener
import com.vl.viewlift.playersdk.model.BroadcastData
import com.vl.viewlift.playersdk.module.data.api.OptionalPlayerParams
import com.vl.viewlift.playersdk.module.data.api.entitlementPoJo.ErrorResponseEntitlement
import com.vl.viewlift.playersdk.utils.VLUtils
import com.vl.viewlift.playersdk.views.VLPlayer

import timber.log.Timber

/**
 * Video player
 *
 * @param uri
 */
@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun HLSVideoPlayer(videoId: String, viewModel: CarousalTrayViewModel = hiltViewModel()) {
    /* Player Views */
    val optionalPlayerParams = OptionalPlayerParams()

    val context = LocalContext.current
    Timber.d("APCMSV2 entered VideoPlayer")
    var vlPlayer: VLPlayer? = null
    val statsView: View? = LayoutInflater.from(context).inflate(R.layout.stats_sample_view, null)

    //Inflate an overlay view and handle it
    val viewOverlay: View? = LayoutInflater.from(context).inflate(R.layout.overlay_view, null)
    val broadcastHorizontalRV: RecyclerView? = viewOverlay?.findViewById(R.id.broadcast_horizontal_rv)
    val rightBroadCastView: View? = LayoutInflater.from(context).inflate(R.layout.overlay_view, null)
    val broadcastVerticalRV: RecyclerView? = rightBroadCastView?.findViewById(R.id.broadcast_horizontal_rv)
    val screenOrientation: VLPlayer.ScreenOrientation? = VLPlayer.ScreenOrientation.PORTRAIT

    var isButtonActionForFullScreen = false
    var layoutParamsP: ConstraintLayout.LayoutParams? = null

    // setUpOverlayValues
    broadcastHorizontalRV?.apply {
        adapter = BroadcastAdapter(
            listOf(
                BroadcastData(videoId, null)
            ), BroadcastAdapter.BroadcastViewType.HORIZONTAL
        )
        layoutManager = LinearLayoutManager(context).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
    }
    broadcastVerticalRV?.apply {
        adapter = BroadcastAdapter(
            listOf(
                BroadcastData(videoId, null)
            ), BroadcastAdapter.BroadcastViewType.VERTICAL
        )
        layoutManager = LinearLayoutManager(context).also {
            it.orientation = LinearLayoutManager.VERTICAL
        }
    }

    DisposableEffect(
        AndroidView(modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .aspectRatio(1.78f, true),
            factory = {
                val playerView =
                    LayoutInflater.from(context).inflate(R.layout.activity_custom_player, null)
                playerView
            },
            update = {playerView->

                val statsButton: ToggleButton? = playerView.findViewById(R.id.stats_button)
                val broadcastButton: ToggleButton? = playerView.findViewById(R.id.broadcast_button)
                var lockScreenButton: ToggleButton? = playerView.findViewById(R.id.lock_screen)
                var fullscreenButton: View? = playerView.findViewById(R.id.full_screen_button)
                var airPlayButton: AppCompatImageButton? = playerView.findViewById(R.id.air_play_button)
                var mediaRouteButton: MediaRouteButton? = playerView.findViewById(R.id.media_route_button)
                //customProgress = findViewById(R.id.exo_progress)
                var pipButton: AppCompatImageButton? = playerView.findViewById(R.id.pip_button)
                var qualityButton: AppCompatTextView? = playerView.findViewById(R.id.quality_button)
                var ccToggleButton: ToggleButton? = playerView.findViewById(com.vl.viewlift.player.R.id.caption_button)

                vlPlayer = playerView.findViewById(R.id.vl_player)

                // initVLPlayer
                vlPlayer?.apply {
                    init(vlOmEventListener)
                    hideAfterTimeOut(2000)
                    setPlayerView(R.layout.player_view_carousel)
                    vlPlayerFullScreenListener = object : VLPlayerFullScreenListener {
                        override fun enterFullScreen(msg: String?) {
                            Timber.d("Enter Full Screen Method called $msg")

//                            viewModel.enterFullScreen()

                            println("" + msg)
                            broadcastButton?.isChecked = false
                            statsButton?.isChecked = false
                            vlPlayer?.resetOverlayContainer()

                            isButtonActionForFullScreen = true
//                            (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            val decorView = (context as Activity).window.decorView
                            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Set the content to appear under the system bars so that the
                                    // content doesn't resize when the system bars hide and show.
                                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    or View.SYSTEM_UI_FLAG_FULLSCREEN)

//                            managePlayerView(true)
                            layoutParamsP = vlPlayer?.layoutParams as ConstraintLayout.LayoutParams
                            vlPlayer?.layoutParams = ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT,
                                ConstraintLayout.LayoutParams.MATCH_PARENT
                            )
                            /*handler.postDelayed({
                                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                            }, 2000)*/
                            setLandscapeView()
                        }

                        override fun exitFullScreen(msg: String?) {
                            Timber.d("Exit Full Screen Method called $msg")
                            println("" + msg)
//                            viewModel.exitFullScreen()
                            broadcastButton?.isChecked = false
                            statsButton?.isChecked = false
                            vlPlayer?.resetOverlayContainer()

                            isButtonActionForFullScreen = true
//                            (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            //showSystemUI();
//                            managePlayerView(false)
                            if (layoutParamsP != null) vlPlayer?.layoutParams = layoutParamsP
                           /* handler.postDelayed({
                                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                            }, 2000)*/
                            setPortraitView()
                        }
                    }
                    shouldAutoPlay(true)
                    shouldPlayMuted(false)
                    setLoop(true)
                    setActivityInstance(context as AppCompatActivity)
                    setOverLayContainer(viewOverlay)
                    setControllerVisibilityListener(object : VLPlayer.VisibilityListener {
                        override fun onVisibilityChange(visibility: Int) {
                            if (visibility != View.VISIBLE) {
                                vlPlayer?.overlayContainerVisibility(View.GONE)
                            }

                            when (screenOrientation) {
                                VLPlayer.ScreenOrientation.LANDSCAPE -> {
                                    Timber.d("Enter Full Screen Method called inside VLPlayer.ScreenOrientation.LANDSCAPE ")

                                    viewModel.enterFullScreen()
                                    if (vlPlayer?.isLockEnable() == true) {
                                        broadcastButton?.visibility = View.GONE
                                        statsButton?.visibility = View.GONE
                                        return
                                    }
                                    if (vlPlayer?.isRightContainerVisible() == true) {
                                        broadcastButton?.visibility = View.VISIBLE
                                        statsButton?.visibility = View.VISIBLE
                                    } else {
                                        broadcastButton?.visibility = visibility
                                        statsButton?.visibility = visibility
                                    }
                                }
                                else -> {
                                    Timber.d("Exit Full Screen Method called inside VLPlayer.ScreenOrientation.LANDSCAPE ")

                                    viewModel.exitFullScreen()
                                    if (visibility != View.VISIBLE)
                                        broadcastButton?.isChecked = false
                                    broadcastButton?.visibility = visibility
                                }
                            }
                        }

                    })
                    setLockStateListener(object : VLPlayer.LockStateChangeListener{
                        override fun onLockStateChange(isEnable: Boolean) {
                            if (isEnable) {
                                broadcastButton?.visibility = View.GONE
                                statsButton?.visibility = View.GONE
                            } else {
                                broadcastButton?.apply {
                                    visibility = View.VISIBLE
                                    isChecked = false
                                }
                                statsButton?.apply {
                                    visibility = View.VISIBLE
                                    isChecked = false
                                }
                            }
                        }
                    })

                    val listener = View.OnClickListener {
                        vlPlayer?.clearRightContainer()
                        when (it.id) {
                            R.id.stats_button -> {
                                broadcastButton?.isChecked = false
                                vlPlayer?.setRightContainerView(statsView)

                            }
                            R.id.broadcast_button -> {
                                statsButton?.isChecked = false
                                vlPlayer?.setRightContainerView(rightBroadCastView)
                            }
                        }

                        VLUtils.ifLet(statsButton, broadcastButton) { (statsButton, broadcastButton) ->
                            when (screenOrientation) {
                                VLPlayer.ScreenOrientation.LANDSCAPE -> {
                                    val visibility = if (!statsButton.isChecked && !broadcastButton.isChecked) {
                                        View.GONE
                                    } else {
                                        View.VISIBLE
                                    }.also { visibility ->
                                        vlPlayer?.disableAutoHide(visibility == View.VISIBLE)
                                        /*playerView?.controller?.disableAutoHide(visibility == View.VISIBLE)*/
                                        //playerViewIVS?.disableAutoHide(visibility == View.VISIBLE)
                                    }
                                    vlPlayer?.rightContainerVisibility(visibility)
                                }
                                else -> {
                                    val visibility = if (broadcastButton.isChecked) {
                                        View.VISIBLE
                                    } else {
                                        View.GONE
                                    }.also { visibility ->
                                        /*playerView?.controller?.disableAutoHide(visibility == View.VISIBLE)
                                        playerViewIVS?.disableAutoHide(visibility == View.VISIBLE)*/
                                        vlPlayer?.disableAutoHide(visibility == View.VISIBLE)
                                    }
                                    vlPlayer?.overlayContainerVisibility(visibility)
                                }
                            }

                        }
                    }
                    statsButton?.setOnClickListener(listener)
                    broadcastButton?.setOnClickListener(listener)
                }

                optionalPlayerParams.isShowThumbImage =
                    false // if you set shouldAutoPlay as false.. you must set setShowThumbImage as false.. otherwise clicking thumnail will start the play.
                // Todo set Playback URL
                val authToken = viewModel.appDataRepository.getAuth()
                if(authToken!=null){
                    vlPlayer?.setSource(
                        authToken,
                        videoId,
                        "tag SamplePlayer Demo",
                        optionalPlayerParams
                    )
                } else {
                    Toast.makeText(context, "Token Expired", Toast.LENGTH_LONG).show()
                }

            }
        )
    ) {
        onDispose {
            vlPlayer?.destroy()
//            exoPlayer.release()
        }
    }
}

private val vlOmEventListener = object : VLOmEventListener {
    override fun firstQuartile() {
        TODO("Not yet implemented")
    }

    override fun midpoint() {
        TODO("Not yet implemented")
    }

    override fun thirdQuartile() {
        TODO("Not yet implemented")
    }

    override fun volumeChange(volume: Float) {
        TODO("Not yet implemented")
    }

    override fun stateChange(state: String) {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun bufferStart() {
        TODO("Not yet implemented")
    }

    override fun bufferEnd() {
        TODO("Not yet implemented")
    }

    override fun startMediaTailor() {
        TODO("Not yet implemented")
    }

    override fun completeMediaTailor() {
        TODO("Not yet implemented")
    }

}

private val vlPlayerVideoEventListener = object : VLPlayerVideoEventListener {
    override fun videoStarted(playerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun videoPaused(currentTime: Long, playerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun videoResumed(currentTime: Long, playerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun videoFinished(currentTime: Long, playerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun videoPlaybackError(
        currentTime: Long,
        errorMessage: String?,
        errorCode: String?,
        playerTag: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun onFullscreenChange(currentTime: Long, isFullscreen: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onSeek(currentTime: Long, playerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun onBuffer(currentTime: Long, playerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun onApiErrorResponse(error: ErrorResponseEntitlement?, playerTag: String?) {
        TODO("Not yet implemented")
    }

    override fun onFullscreenButtonClick(vlPlayer: VLPlayer?, playerTag: String?) {
        TODO("Not yet implemented")
    }

}

private val vlplayerCustomEventListener = object : VLPlayerCustomEventListener {
    override fun onTitleTextSet(title: String?) {
        TODO("Not yet implemented")
    }

    override fun onPlayerViewVisible(durationTime: Long, positionTime: Long) {
        TODO("Not yet implemented")
    }

}
