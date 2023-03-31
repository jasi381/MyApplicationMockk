package com.viewlift.uimodule.schedule.ui.component.day

import androidx.compose.ui.graphics.Color

data class CustomCalenderDayColors(
    val textColor: Color, // Default Text Color
    val selectedTextColor: Color, // Selected Text Color
)

object CustomCalenderDayDefaultColors {

    fun defaultColors() = CustomCalenderDayColors(Color.White, Color.White)
}
