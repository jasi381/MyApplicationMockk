package com.viewlift.uimodule.schedule.ui.customCalender

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.viewlift.common.label.BootstrapColors
import com.viewlift.common.utils.parse
import com.viewlift.uimodule.schedule.model.CustomCalenderDay
import com.viewlift.uimodule.schedule.model.CustomCalenderEvent
import com.viewlift.uimodule.schedule.model.toCustomerCalenderDay
import com.viewlift.uimodule.schedule.ui.color.CustomCalenderThemeColor
import com.viewlift.uimodule.schedule.ui.component.day.CustomCalenderDayColors
import com.viewlift.uimodule.schedule.ui.component.header.CustomCalenderHeader
import com.viewlift.uimodule.schedule.ui.component.header.CustomCalenderHeaderConfig
import com.viewlift.uimodule.schedule.ui.component.text.CustomCalenderNormalText
import com.viewlift.uimodule.schedule.ui.component.text.CustomCalenderTextColor
import com.viewlift.uimodule.schedule.ui.component.text.CustomCalenderTextConfig
import com.viewlift.uimodule.schedule.ui.component.text.CustomCalenderTextSize
import com.viewlift.uimodule.schedule.utils.Constant
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.todayIn

val WeekDays = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun CustomCalenderImpl(
    modifier: Modifier = Modifier,
    takeMeToDate: LocalDate?,
    customCalenderDayColors: CustomCalenderDayColors,
    customCalenderHeaderConfig: CustomCalenderHeaderConfig? = null,
    customCalenderThemeColors: List<CustomCalenderThemeColor>,
    customCalenderEvents: List<CustomCalenderEvent> = emptyList(),
    onCurrentDayClick: (CustomCalenderDay, List<CustomCalenderEvent>) -> Unit = { _, _ -> },
) {
    val currentDay = takeMeToDate ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
    val displayedMonth = remember {
        mutableStateOf(currentDay.month)
    }
    val displayedYear = remember {
        mutableStateOf(currentDay.year)
    }
    val currentMonth = displayedMonth.value
    val currentYear = displayedYear.value

    val daysInMonth = currentMonth.minLength()
    val monthValue =
        if (currentMonth.value.toString().length == 1) "0" + currentMonth.value.toString() else currentMonth.value.toString()
    val startDayOfMonth = "$currentYear-$monthValue-01".toLocalDate()
    val firstDayOfMonth = startDayOfMonth.dayOfWeek
    val selectedCustomCalenderDate = remember { mutableStateOf(currentDay) }
    val newKalenderHeaderConfig = CustomCalenderHeaderConfig(
        customCalenderTextConfig = CustomCalenderTextConfig(
            customCalenderTextSize = CustomCalenderTextSize.SubTitle,
            customCalenderTextColor = CustomCalenderTextColor(
                customCalenderThemeColors[currentMonth.value.minus(1)].headerTextColor,
            )
        )
    )

    Column(
        modifier = modifier
            .background(
                //color changed for the background
                color = BootstrapColors.generalBackground.parse
            )
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        CustomCalenderHeader(
            modifier = Modifier,
            month = displayedMonth.value,
            onPreviousClick = {
                if (displayedMonth.value.value == 1) {
                    displayedYear.value = displayedYear.value.minus(1)
                }
                displayedMonth.value = displayedMonth.value.minus(1)
            },
            onNextClick = {
                if (displayedMonth.value.value == 12) {
                    displayedYear.value = displayedYear.value.plus(1)
                }
                displayedMonth.value = displayedMonth.value.plus(1)
            },
            year = displayedYear.value,
            customCalenderHeaderConfig = customCalenderHeaderConfig ?: newKalenderHeaderConfig
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            columns = GridCells.Fixed(7),
            content = {
                items(WeekDays) {
                    CustomCalenderNormalText(
                        text = it,
                        fontWeight = FontWeight.Normal,
                        textColor = "#838996".parse,
                    )
                }
                items((getInitialDayOfMonth(firstDayOfMonth)..daysInMonth).toList()) {
                    if (it > 0) {
                        val day = getGeneratedDay(it, currentMonth, currentYear)
                        val isCurrentDay = day == currentDay
                        com.viewlift.uimodule.schedule.ui.component.day.CustomCalenderDay(
                            customCalenderDay = day.toCustomerCalenderDay(),
                            modifier = Modifier.padding(9.dp),
                            customCalenderEvents = customCalenderEvents,
                            isCurrentDay = isCurrentDay,
                            onCurrentDayClick = { customCalenderDay, events ->
                                selectedCustomCalenderDate.value = customCalenderDay.localDate
                                onCurrentDayClick(customCalenderDay, events)
                            },
                            selectedCustomCalenderDay = selectedCustomCalenderDate.value,
                            customCalenderDayColors = customCalenderDayColors,
                            dotColor = customCalenderThemeColors[currentMonth.value.minus(1)].headerTextColor,
                            dayBackgroundColor = customCalenderThemeColors[currentMonth.value.minus(1)].dayBackgroundColor,
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun CustomCalenderImpl(
    modifier: Modifier = Modifier,
    customCalenderEvents: List<CustomCalenderEvent> = emptyList(),
    onCurrentDayClick: (CustomCalenderDay, List<CustomCalenderEvent>) -> Unit = { _, _ -> },
    takeMeToDate: LocalDate?,
    customCalenderDayColors: CustomCalenderDayColors,
    customCalenderThemeColor: CustomCalenderThemeColor,
    customCalenderHeaderConfig: CustomCalenderHeaderConfig? = null
) {
    val currentDay = takeMeToDate ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
    val displayedMonth = remember {
        mutableStateOf(currentDay.month)
    }
    val displayedYear = remember {
        mutableStateOf(currentDay.year)
    }
    val currentMonth = displayedMonth.value
    val currentYear = displayedYear.value

    val daysInMonth = currentMonth.minLength()
    val monthValue =
        if (currentMonth.value.toString().length == 1) "0" + currentMonth.value.toString() else currentMonth.value.toString()
    val startDayOfMonth = "$currentYear-$monthValue-01".toLocalDate()
    val firstDayOfMonth = startDayOfMonth.dayOfWeek
    val selectedCustomCalenderDate = remember { mutableStateOf(currentDay) }
    val newKalenderHeaderConfig = CustomCalenderHeaderConfig(
        CustomCalenderTextConfig(
            customCalenderTextColor = CustomCalenderTextColor(
                customCalenderThemeColor.headerTextColor
            ),
            customCalenderTextSize = CustomCalenderTextSize.SubTitle
        )
    )

    Column(
        modifier = modifier
            .background(
                color = customCalenderThemeColor.backgroundColor
            )
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        CustomCalenderHeader(
            modifier = Modifier,
            month = displayedMonth.value,
            onPreviousClick = {
                if (displayedMonth.value.value == 1) {
                    displayedYear.value = displayedYear.value.minus(1)
                }
                displayedMonth.value = displayedMonth.value.minus(1)
            },
            onNextClick = {
                if (displayedMonth.value.value == 12) {
                    displayedYear.value = displayedYear.value.plus(1)
                }
                displayedMonth.value = displayedMonth.value.plus(1)
            },
            year = displayedYear.value,
            customCalenderHeaderConfig = customCalenderHeaderConfig ?: newKalenderHeaderConfig
        )

        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(7),
            content = {
                items(WeekDays) {
                    CustomCalenderNormalText(
                        text = it,
                        fontWeight = FontWeight.Normal,
                        textColor = customCalenderDayColors.textColor,
                    )
                }
                items((getInitialDayOfMonth(firstDayOfMonth)..daysInMonth).toList()) {
                    if (it > 0) {
                        val day = getGeneratedDay(it, currentMonth, currentYear)
                        val isCurrentDay = day == currentDay
                        com.viewlift.uimodule.schedule.ui.component.day.CustomCalenderDay(
                            customCalenderDay = day.toCustomerCalenderDay(),
                            modifier = Modifier,
                            customCalenderEvents = customCalenderEvents,
                            isCurrentDay = isCurrentDay,
                            onCurrentDayClick = { customCalenderDay, events ->
                                selectedCustomCalenderDate.value = customCalenderDay.localDate
                                onCurrentDayClick(customCalenderDay, events)
                            },
                            selectedCustomCalenderDay = selectedCustomCalenderDate.value,
                            customCalenderDayColors = customCalenderDayColors,
                            dotColor = customCalenderThemeColor.headerTextColor,
                            dayBackgroundColor = customCalenderThemeColor.dayBackgroundColor,
                        )
                    }
                }
            }
        )
    }
}

private fun getInitialDayOfMonth(firstDayOfMonth: DayOfWeek) = -(firstDayOfMonth.value).minus(2)

private fun getGeneratedDay(day: Int, currentMonth: Month, currentYear: Int): LocalDate {
    val monthValue =
        if (currentMonth.value.toString().length == 1) "0${currentMonth.value}" else currentMonth.value.toString()
    val newDay = if (day.toString().length == 1) "0$day" else day
    return "$currentYear-$monthValue-$newDay".toLocalDate()
}
