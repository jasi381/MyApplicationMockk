package com.viewlift.uimodule.schedule

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.viewlift.common.label.BootstrapColors
import com.viewlift.common.label.ScheduleModuleColors
import com.viewlift.common.label.ScheduleModuleLabels
import com.viewlift.common.utils.parse
import com.viewlift.core.extensions.collectAsStateWithLifecycle
import com.viewlift.uimodule.data.Layout
import com.viewlift.uimodule.data.Module
import com.viewlift.uimodule.schedule.model.GameDetails
import com.viewlift.uimodule.schedule.model.TeamDetails
import com.viewlift.uimodule.tray.util.TrayUtil
import com.viewlift.uimodule.uistate.RefreshUiState
import com.viewlift.uimodule.utils.UiUtils
import com.viewlift.uimodule.viewmodel.CarousalTrayViewModel
import com.viewlift.uimodule.viewmodel.HomeAwayTeamScore
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LiveScheduleModule(item: Module, viewModel: CarousalTrayViewModel = hiltViewModel()) {

    viewModel.initScheduleModule(item)


    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(BootstrapColors.generalBackground.parse)
    ) {
        var btnState by remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(){
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = item.title ?: "",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .size(15.dp)
                        .clickable {
                            btnState = !btnState
                        },
                    painter = painterResource(id = if (btnState) com.viewlift.common.R.drawable.ic_close_white else com.viewlift.common.R.drawable.ic_calender),
                    contentDescription = null,
                    tint = Color.White,
                )
                Spacer(modifier = Modifier.width(10.dp))
                if(!btnState){
                    Text(
                        text = ScheduleModuleLabels.fullScheduleText ,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            btnState = !btnState
                        }
                    )
                }
            }

        }
        AnimatedVisibility(visible = btnState) {
            CustomCalenderLib()
        }

        val listOfSchedules: List<GameDetails> = getListOfScheduleItems(item)
        viewModel.listOfSchedules = listOfSchedules

        viewModel.setListOfGames(listOfSchedules)
        ListOfSchedules(item.layout)
        HandleUIState()
    }
}

@Composable
fun HandleUIState(viewModel: CarousalTrayViewModel = hiltViewModel()) {
    val uiState: RefreshUiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                color = BootstrapColors.progressBarColor.parse,
                strokeWidth = Dp(value = 4F)
            )
        }
    } else if (uiState.scheduleList != null) {

        val newListOfSchedules = uiState.scheduleList!!.items.map {
            GameDetails (
                gameType = it.currentState ?: "pre",
                teamDetails = TeamDetails (
                    homeTeamImage = it.homeTeam?.gist?.imageGist?._16x9,
                    homeTeamName = it.homeTeam?.shortName,
                    homeTeamScore = null,
                    awayTeamImage = it.awayTeam?.gist?.imageGist?._16x9,
                    awayTeamName = it.awayTeam?.shortName,
                    awayTeamScore = null
               ),
                gameId = it.id ?: "",
                gameStartTime = it.schedules.first().startDate ?: 0L,
                broadcaster = it.broadcaster ?: ""
            )
        }

        viewModel.setListOfGames(newListOfSchedules)

    } else if (uiState.isError && uiState.error != null) {


        uiState.isError = false
    }
}

@Composable
private fun ListOfSchedules(layout: Layout?, viewModel: CarousalTrayViewModel = hiltViewModel()) {

    val listOfGames by viewModel.listOfGames.collectAsStateWithLifecycle(initialValue = null)

    if(listOfGames!=null){
        LazyRow(
            Modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
        ) {
            items(listOfGames!!){
                GameStats(gameDetails = it, layout = layout)
            }
        }
    }

}

fun getListOfScheduleItems(item: Module):  List<GameDetails> {

    val thumbnailType = item.layout?.settings?.thumbnailType

    val listOfSchedules: List<GameDetails>? = item.contentData.map {

        val homeTeamImageGist = it.homeTeam?.gist?.imageGist
        val homeTeamName = UiUtils.getScheduleHomeTeamName(it.homeTeam?.shortName, it.homeTeam?.gist?.title)
        val homeTeamScore = it.score?.homePoint

        val awayTeam = it.awayTeam?.gist?.imageGist
        val awayTeamName = UiUtils.getScheduleAwayTeamName(it.awayTeam?.shortName, it.awayTeam?.gist?.title)
        val awayTeamScore = it.score?.awayPoint

        val currentGameState = it.currentState ?: "end"

        val homeTeamImageUrl = TrayUtil.getOrientationType(
            homeTeamImageGist,
            thumbnailType
        )

        val awayTeamImageUrl = TrayUtil.getOrientationType(
            awayTeam,
            thumbnailType
        )

        GameDetails (
            gameType = currentGameState,
            teamDetails = TeamDetails(
                homeTeamImage = homeTeamImageUrl,
                homeTeamName = homeTeamName,
                homeTeamScore = homeTeamScore,
                awayTeamImage = awayTeamImageUrl,
                awayTeamName = awayTeamName,
                awayTeamScore = awayTeamScore
            ),
            gameId = it.id ?: "",
            gameStartTime = if (it.schedules.isNotEmpty()) it.schedules.first().startDate ?: 0L else 0L,
            broadcaster = it.broadcaster ?: ""
        )
    }

    return listOfSchedules ?: emptyList()
}

@Composable
fun GameStats(gameDetails: GameDetails,layout: Layout?) {
    val configuration = LocalConfiguration.current

    val thirtyPercentScreenWidth = configuration.screenWidthDp * .25

    val screenWidth = configuration.screenWidthDp.dp - thirtyPercentScreenWidth.dp // 70% screen width
    Log.e("ndn", screenWidth.value.toString())
    Column(
        modifier = Modifier
            .width(screenWidth)
            .padding(horizontal = 5.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(ScheduleModuleColors.moduleBackgroundColor.parse),
    ) {
        StartedGameHeaderUi(gameDetails)
        Spacer(modifier = Modifier.height(15.dp))
        TeamDetailLive(layout,gameDetails, checkHighestScoreTeam(gameDetails.teamDetails))
        Spacer(modifier = Modifier.height(15.dp))
        ScheduleButtonLayout(gameDetails)
        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Composable
fun StartedGameHeaderUi(gameDetails: GameDetails) {
    val simpleDateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
    val dateString: String = simpleDateFormat.format(gameDetails.gameStartTime * 1000L)

    val hoursDateFormat = SimpleDateFormat("hh:mmaa")
    val hour: String = (hoursDateFormat.format(gameDetails.gameStartTime * 1000L)).lowercase()
    val headerColor: Color
    val headerTitle: String

    when (gameDetails.gameType) {
        "live" -> {
            headerColor = ScheduleModuleColors.liveGameColor.parse
            headerTitle = ScheduleModuleLabels.liveScheduleTitle.uppercase()
        }
        "pre" -> {
            headerColor = ScheduleModuleColors.preGameColor.parse
            headerTitle = hour
        }
        "default" -> {
            headerColor = ScheduleModuleColors.preGameColor.parse
            headerTitle = hour
        }
        else -> {
            headerColor = ScheduleModuleColors.postGameColor.parse
            headerTitle = ScheduleModuleLabels.postScheduleTitle
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = headerColor
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = dateString,
                    color = Color.White,
                    maxLines = 1,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = headerTitle,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
            Text(
                text = gameDetails.broadcaster,
                color = Color.White,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ScheduleButtonLayout(gameDetails: GameDetails) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        val buttonModifier = Modifier
                            .border(1.dp, ScheduleModuleColors.postGameColor.parse)
                            .background(ScheduleModuleColors.moduleBackgroundColor.parse)
                            .height(35.dp)
                            .clip(RoundedCornerShape(10.dp))

        Box(modifier = buttonModifier
            .weight(1.25f)
            .fillMaxWidth(),
            contentAlignment = Alignment.Center) {
            Text(
                text = ScheduleModuleLabels.goToGameCenterText,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(.75f)){

            Box(modifier = buttonModifier
                    .weight(1f)
                    .width(35.dp),
                contentAlignment = Alignment.Center,

            ) {
                Image(
                    painter = painterResource(com.viewlift.common.R.drawable.ic_schedule_notify),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = buttonModifier
                    .weight(1f)
                    .width(35.dp),
                contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(com.viewlift.common.R.drawable.ic_ticket),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                )
            }
        }

    }
}


@Composable
fun TeamDetailLive(layout: Layout?, gameDetails: GameDetails, result: String, viewModel: CarousalTrayViewModel = hiltViewModel()) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    Log.e("Schedule Live score", "${events} ")

    val scores: HomeAwayTeamScore? = viewModel.getGameScore(gameDetails.gameId)

    val homeGameScore = scores?.homeTeamScore ?: 0
    val awayGameScore = scores?.awayTeamScore ?: 0

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(.75f)) {

                AsyncImage(
                    model = gameDetails.teamDetails.awayTeamImage ?: viewModel.placeholderImage,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(3.dp)),
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = gameDetails.teamDetails.awayTeamName?.uppercase(Locale.getDefault()) ?: "",
                    maxLines = 1,
                    style = TrayUtil.textStyle(layout,"team"),
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }
            Text(
                text = if(awayGameScore==0) gameDetails.teamDetails.awayTeamScore?.toString() ?: "" else awayGameScore.toString(),
                textAlign = TextAlign.End,
                color = Color.White,
                style = TrayUtil.textStyle(layout,"team"),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(.25f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(0.75f)) {

                AsyncImage(
                    model = gameDetails.teamDetails.homeTeamImage ?: viewModel.placeholderImage,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(3.dp)),
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = gameDetails.teamDetails.homeTeamName?.uppercase(Locale.getDefault()) ?: "",
                    style = TrayUtil.textStyle(layout,"team"),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }
            Text(
                text = if(homeGameScore==0) gameDetails.teamDetails.homeTeamScore?.toString() ?: "" else homeGameScore.toString(),
                maxLines = 1,
                style = TrayUtil.textStyle(layout,"team"),
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis,
                color =  Color.White,
                modifier = Modifier.weight(0.25f)
            )
        }
    }
}

fun checkHighestScoreTeam(teamDetails: TeamDetails): String {
    if (teamDetails.homeTeamScore!=null && teamDetails.awayTeamScore!=null && teamDetails.homeTeamScore  > teamDetails.awayTeamScore!!) {
        return "TEAM1"
    } else if (teamDetails.homeTeamScore!=null && teamDetails.awayTeamScore!=null && teamDetails.homeTeamScore < teamDetails.awayTeamScore) {
        return "TEAM2"
    } else {
        return "TIE"
    }

}


//@Preview
//@Composable
//fun GameStatePreview() {
//    LiveScheduleModule(null)
//}