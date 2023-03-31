package com.viewlift.uimodule.carousal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.viewlift.common.label.BootstrapColors
import com.viewlift.common.utils.parse
import com.viewlift.core.extensions.collectAsStateWithLifecycle
import com.viewlift.uimodule.data.ContentData
import com.viewlift.uimodule.data.Layout
import com.viewlift.uimodule.tray.util.TrayUtil
import com.viewlift.uimodule.utils.UiUtils
import com.viewlift.uimodule.viewmodel.CarousalTrayViewModel
import com.viewlift.uimodule.viewmodel.GameChangeState
import com.viewlift.uimodule.viewmodel.HomeAwayTeamScore

@Composable
fun CarouselLiveScore(
    buttonColor: Color = Color.Red,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier,
    contentData: ContentData?,
    trayLayout: Layout,
    viewModel: CarousalTrayViewModel = hiltViewModel()) {
    val shape = RoundedCornerShape(8.dp)
    val events by viewModel.events.collectAsStateWithLifecycle()
    Log.e("Game Live score", "${events} ")

    val scores: HomeAwayTeamScore? = viewModel.getGameScore(contentData?.id)

    val homeGameScore = scores?.homeTeamScore
    val awayGameScore = scores?.awayTeamScore

    val game: GameChangeState? = viewModel.getGameState(contentData?.id)

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clip(shape)) {
        Spacer(modifier = Modifier.height(15.dp))

        Row {
            AsyncImage(
                model = contentData?.awayTeam?.gist?.imageGist?.r1x1 ?: viewModel.placeholderImage,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(30.dp)
                    .weight(.10f)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = UiUtils.getAwayTeamName(contentData),
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TrayUtil.textStyle(trayLayout,"team"),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically)
                    .weight(0.7f)
            )
            Text(
                text = getGameScore(
                    score = awayGameScore?.toString() ?: (contentData?.score?.awayPoint?.toString() ?: ""),
                    status = game?.gameState
                ),
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TrayUtil.textStyle(trayLayout,"team"),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.CenterVertically)
                    .weight(.20f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = contentData?.homeTeam?.gist?.imageGist?.r1x1 ?: viewModel.placeholderImage,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(30.dp)
                    .weight(.10f)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = UiUtils.getHomeTeamName(contentData),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TrayUtil.textStyle(trayLayout,"team"),
                color = Color.White,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically)
                    .weight(0.7f)
            )
            Text(
                text = getGameScore(
                    score = homeGameScore?.toString() ?: (contentData?.score?.homePoint?.toString() ?: ""),
                    status = game?.gameState
                ),
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TrayUtil.textStyle(trayLayout,"team"),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.CenterVertically)
                    .weight(.20f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
private fun getGameScore(
    score: String,
    status: String?
) : String {
    return if (status == "end"){
        ""
    } else {
        score
    }
}