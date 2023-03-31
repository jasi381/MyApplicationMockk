package com.viewlift.uimodule.schedule

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.viewlift.common.label.ScheduleModuleColors
import com.viewlift.common.utils.parse
import com.viewlift.uimodule.schedule.model.CustomCalenderDay
import com.viewlift.uimodule.schedule.model.CustomCalenderEvent
import com.viewlift.uimodule.schedule.ui.color.CustomCalenderColors
import com.viewlift.uimodule.schedule.ui.color.CustomCalenderThemeColor
import com.viewlift.uimodule.schedule.ui.component.day.CustomCalenderDayColors
import com.viewlift.uimodule.schedule.ui.component.day.CustomCalenderDayDefaultColors
import com.viewlift.uimodule.schedule.ui.component.header.CustomCalenderHeaderConfig
import com.viewlift.uimodule.schedule.ui.customCalender.CustomCalenderImpl
import com.viewlift.uimodule.viewmodel.CarousalTrayViewModel

import kotlinx.datetime.LocalDate
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun CustomCalenderLib(viewModel: CarousalTrayViewModel = hiltViewModel()) {

    val listOfDots = viewModel.listOfSchedules.map {

        val liveTimeInMillis = it.gameStartTime.toLong() * 1000

        val calender = Calendar.getInstance()
        calender.timeInMillis = liveTimeInMillis
//
        val month = calender.get(Calendar.MONTH) + 1
//
//        val month_date = SimpleDateFormat("MMM")
//        val monthName = month_date.format(calender.getTime())

        val year = calender.get(Calendar.YEAR)
//        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)


        CustomCalenderEvent(
            LocalDate(year, month,day), "Game",
        )
    }

    Column(
        Modifier
            .wrapContentSize()
            .height(380.dp)
            .clip(RoundedCornerShape(7.dp))
            .padding(20.dp)
    ) {

        CustomCalender(
            modifier = Modifier
                .border(BorderStroke(2.dp, ScheduleModuleColors.preGameColor.parse)),
            onCurrentDayClick = { calender, listOfEvents ->

                viewModel.updateScheduleEvents(calender)

                Log.e("ddjndn","Selected dated "+ calender.localDate.toString())
            },
            customCalenderEvents = listOfDots
        )
    }
}

@Composable
fun CustomCalender(
    modifier: Modifier = Modifier,
    customCalenderEvents: List<CustomCalenderEvent> = emptyList(),
    customCalenderThemeColors: List<CustomCalenderThemeColor> = CustomCalenderColors.defaultColors(),
    onCurrentDayClick: (CustomCalenderDay, List<CustomCalenderEvent>) -> Unit = { _, _ -> },
    customCalenderDayColors: CustomCalenderDayColors = CustomCalenderDayDefaultColors.defaultColors(),
    customCalenderHeaderConfig: CustomCalenderHeaderConfig? = null,
    takeMeToDate: LocalDate? = null,
) {
    if (customCalenderThemeColors.isEmpty() || customCalenderThemeColors.count() < 12) throw Exception("CustomCalenderThemeColor cannot be null or less than 12, If you want to use same color accors months pass CustomCalenderThemeColor = CustomCalenderThemeColor(values)")

    CustomCalenderImpl(
        modifier = modifier.wrapContentHeight(),
        customCalenderEvents = customCalenderEvents,
        onCurrentDayClick = onCurrentDayClick,
        customCalenderDayColors = customCalenderDayColors,
        customCalenderThemeColors = customCalenderThemeColors,
        takeMeToDate = takeMeToDate,
        customCalenderHeaderConfig = customCalenderHeaderConfig
    )
}

