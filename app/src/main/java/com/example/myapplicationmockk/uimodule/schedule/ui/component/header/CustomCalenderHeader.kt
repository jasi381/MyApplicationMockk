package com.viewlift.uimodule.schedule.ui.component.header

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.viewlift.common.label.ScheduleModuleColors
import com.viewlift.common.utils.parse
import com.viewlift.uimodule.schedule.ui.component.button.CustomCalenderIconButton
import com.viewlift.uimodule.schedule.ui.component.text.CustomCalenderSubTitle
import java.time.Month
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomCalenderHeader(
    month: Month,
    year: Int,
    modifier: Modifier = Modifier,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    arrowShown: Boolean = true,
    customCalenderHeaderConfig: CustomCalenderHeaderConfig
) {
    val isNext = remember { mutableStateOf(true) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(2.dp, ScheduleModuleColors.defaultGameColor.parse))
            .wrapContentHeight()
            .padding(2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //intial start of the project
        if (arrowShown) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CustomCalenderIconButton(
                    modifier = Modifier.wrapContentSize(),
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous Week",
                    onClick = {
                        isNext.value = false
                        onPreviousClick()
                    }

                )

                AnimatedContent(
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .align(Alignment.CenterVertically),
                    targetState = getTitleText(month, year),
                    transitionSpec = {
                        addAnimation(isNext = isNext.value).using(
                            SizeTransform(clip = false)
                        )
                    }
                )
                {
                    CustomCalenderSubTitle(
                        text = it,
                        modifier = Modifier,
                        customCalenderTextConfig = customCalenderHeaderConfig.customCalenderTextConfig
                    )
                }

                CustomCalenderIconButton(
                    modifier = Modifier.wrapContentSize(),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next Month",
                    onClick = {
                        isNext.value = true
                        onNextClick()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
internal fun addAnimation(duration: Int = 500, isNext: Boolean): ContentTransform {
    return slideInHorizontally(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) height else -height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutHorizontally(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) -height else height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )

}

internal fun getTitleText(month: Month, year: Int): String {
    return month.getDisplayName(TextStyle.FULL, Locale.getDefault()).lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    } + " " + year
}
