package com.viewlift.uimodule.schedule.ui.component.day

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.layoutId
import com.viewlift.common.label.BootstrapColors
import com.viewlift.common.utils.parse
import com.viewlift.uimodule.schedule.model.CustomCalenderDay
import com.viewlift.uimodule.schedule.model.CustomCalenderEvent
import com.viewlift.uimodule.schedule.ui.component.text.CustomCalenderNormalText
import com.viewlift.uimodule.schedule.utils.Constant

import kotlinx.datetime.LocalDate

@Composable
fun CustomCalenderDay(
    customCalenderDay: CustomCalenderDay,
    selectedCustomCalenderDay: kotlinx.datetime.LocalDate,
    customCalenderDayColors: CustomCalenderDayColors,
    dotColor: Color,
    dayBackgroundColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    textSize: TextUnit = 12.sp,
    customCalenderEvents: List<CustomCalenderEvent> = emptyList(),
    isCurrentDay: Boolean = false,
    onCurrentDayClick: (CustomCalenderDay, List<CustomCalenderEvent>) -> Unit = { _, _ -> },

    ) {
    val customCalenderDayState = getCustomCalenderDayState(selectedCustomCalenderDay, customCalenderDay.localDate)
    val bgColor = getBackgroundColor(customCalenderDayState, dayBackgroundColor)
    val textColor = getTextColor(customCalenderDayState, customCalenderDayColors)
    val weight = getTextWeight(customCalenderDayState)
    val border = getBorder(isCurrentDay)

    val constraints= ConstraintSet {
        val NormalText = createRefFor("CustomCalenderNormalText")
        val Row = createRefFor("Row")

        constrain(NormalText){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)

            width= Dimension.wrapContent
            height = Dimension.wrapContent
        }
        constrain(Row){
            top.linkTo(NormalText.bottom)
            width= Dimension.wrapContent
            height = Dimension.wrapContent
        }
    }

    Box(
        modifier = modifier
            .border(border = border, shape = CircleShape)
            .clip(shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) { onCurrentDayClick(customCalenderDay, customCalenderEvents) }
            .size(size = size)
            .background(color = bgColor),
        contentAlignment = Alignment.Center
    ) {
        ConstraintLayout(
            constraints,
            modifier = Modifier
                .fillMaxSize()
        ){
            CustomCalenderNormalText(
                text = customCalenderDay.localDate.dayOfMonth.toString(),
                modifier = Modifier.layoutId("CustomCalenderNormalText"),
                fontWeight = weight,
                textColor = textColor,
                textSize = textSize,
            )

            Row(
                modifier = Modifier
                    .layoutId("Row")
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val customCalenderEventForDay = customCalenderEvents.filter { it.date == customCalenderDay.localDate }
                if (customCalenderEventForDay.isNotEmpty()) {
                    CustomCalenderDots (
                        modifier = Modifier, index = 0, size = size, color = dotColor
                    )
                }
            }
        }

    }
}

@Composable
fun CustomCalenderDots(
    modifier: Modifier = Modifier,
    index: Int,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .padding(horizontal = 0.dp)
            .clip(shape = CircleShape)
            .background(
                color = BootstrapColors.ctaBGColor.parse
            )
            .size(size = 3.dp)
    )
}

private fun getCustomCalenderDayState(selectedDate: LocalDate, currentDay: LocalDate) =
    when (selectedDate) {
        currentDay -> CustomCalenderDayState.CustomCalenderDaySelected
        else -> CustomCalenderDayState.CustomCalenderDayDefault
    }

private fun getBorder(isCurrentDay: Boolean) = BorderStroke(
    width = if (isCurrentDay) 1.dp else 0.dp,
    color = if (isCurrentDay) Color.White else Color.Transparent,
)

private fun getTextWeight(customCalenderDayState: CustomCalenderDayState) =
    if (customCalenderDayState is CustomCalenderDayState.CustomCalenderDaySelected) {
        FontWeight.Bold
    } else {
        FontWeight.SemiBold
    }

private fun getBackgroundColor(
    customCalenderDayState: CustomCalenderDayState,
    dayBackgroundColor: Color
) = if (customCalenderDayState is CustomCalenderDayState.CustomCalenderDaySelected) {
    Constant.selectedDotColor
} else {
    Color.Transparent
}

private fun getTextColor(
    customCalenderDayState: CustomCalenderDayState,
    customCalenderDayColors: CustomCalenderDayColors,
): Color = if (customCalenderDayState is CustomCalenderDayState.CustomCalenderDaySelected) {
    customCalenderDayColors.selectedTextColor
} else {
    customCalenderDayColors.textColor
}
