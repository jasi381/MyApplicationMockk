package com.viewlift.uimodule.videoPlayer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.mediarouter.app.MediaRouteButton
import com.viewlift.common.ui.composable.OnLifecycleEvent
import com.viewlift.uimodule.R
import com.viewlift.uimodule.viewmodel.CarousalTrayViewModel
import com.vl.viewlift.playersdk.events.VLPlayerFullScreenListener
import com.vl.viewlift.playersdk.module.data.api.OptionalPlayerParams
import com.vl.viewlift.playersdk.views.VLPlayer
import timber.log.Timber

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayerView(videoId: String, viewModel: CarousalTrayViewModel = hiltViewModel()) {
    var vlPlayer: VLPlayer? = null
    val context = LocalContext.current as Activity
    val optionalPlayerParams = OptionalPlayerParams()
    var isFullScreen by remember { mutableStateOf(false) }
    val windowInsetsController =
        WindowCompat.getInsetsController(context.window, context.window.decorView)
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    var layoutParamsP: ViewGroup.LayoutParams? = null

    OnLifecycleEvent { owner, event ->
        // do stuff on event
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                vlPlayer?.pause()
            }
            Lifecycle.Event.ON_RESUME -> {
                vlPlayer?.play()
            }
            Lifecycle.Event.ON_DESTROY -> {
                vlPlayer?.pause()
                vlPlayer?.destroy()
            }

            else  -> { /* other stuff */ }
        }
    }

    DisposableEffect(
        AndroidView(modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .aspectRatio(1.78f, true),
            factory = {
               /* val playerView =
                    LayoutInflater.from(context).inflate(R.layout.activity_custom_player, null)
                playerView*/
                vlPlayer = VLPlayer(context = context)
                vlPlayer!!
            }, update = { player ->
              //  vlPlayer = playerView.findViewById(R.id.vl_player)
                player.apply {
                    init()
                    hideAfterTimeOut(2000)
                    setActivityInstance(context as AppCompatActivity)
                    setPlayerView(R.layout.player_view_sample_6_with_different_ids)
                    shouldAutoPlay(true)
                    shouldPlayMuted(true)
                    setLoop(true)
                    updateUiForPortrait(player)

                    vlPlayerFullScreenListener = object : VLPlayerFullScreenListener {
                        override fun enterFullScreen(msg: String?) {
                            viewModel.toggleFullScreenPlayer(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                            setScreenOrientation(context, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                            layoutParamsP = player.getLayoutParams() as ViewGroup.LayoutParams
                            player.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            player.playerView?.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                            updateUiForLandscape(player)
                            player.shouldPlayMuted(false)
                        }

                        override fun exitFullScreen(msg: String?) {
                            viewModel.toggleFullScreenPlayer(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            setScreenOrientation(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            if (layoutParamsP != null) player.setLayoutParams(layoutParamsP)
                            updateUiForPortrait(player)
                            player.shouldPlayMuted(true)
                        }

                    }

                }
                val authToken = viewModel.appDataRepository.getAuth()
                if(authToken!=null){
                    player?.setSource(
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
            Timber.d("PlayerDestroyed VideoID $videoId")
            vlPlayer?.pause()
            vlPlayer?.destroy()
            viewModel.currentPage = null
//            exoPlayer.release()
        }
    }
}


fun setScreenOrientation(context: Context, orientation: Int) {
    val activity = context as Activity ?: return
    activity.requestedOrientation = orientation
    if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        hideSystemUi(context)
    } else {
        showSystemUi(context)
    }
}

fun hideSystemUi(context: Context) {
    val activity = context as Activity ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun showSystemUi(context: Context) {
    val activity = context as Activity ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}

fun updateUiForLandscape(vlPlayer: VLPlayer){
    vlPlayer.findViewById<RelativeLayout>(R.id.exo_controller_container)?.visibility = View.VISIBLE
    vlPlayer.findViewById<AppCompatImageButton>(R.id.pip_button)?.visibility = View.VISIBLE
    vlPlayer.findViewById<MediaRouteButton>(R.id.media_route_button)?.visibility = View.VISIBLE
    vlPlayer.findViewById<AppCompatImageButton>(R.id.air_play_button)?.visibility = View.VISIBLE
    vlPlayer.findViewById<LinearLayout>(R.id.seek_bar_parent)?.visibility = View.VISIBLE
}

fun updateUiForPortrait(vlPlayer: VLPlayer){
    vlPlayer.findViewById<RelativeLayout>(R.id.exo_controller_container)?.visibility = View.GONE
    vlPlayer.findViewById<AppCompatImageButton>(R.id.pip_button)?.visibility = View.GONE
    vlPlayer.findViewById<MediaRouteButton>(R.id.media_route_button)?.visibility = View.GONE
    vlPlayer.findViewById<AppCompatImageButton>(R.id.air_play_button)?.visibility = View.GONE
    vlPlayer.findViewById<LinearLayout>(R.id.seek_bar_parent)?.visibility = View.GONE
}