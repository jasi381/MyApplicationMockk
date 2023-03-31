package com.viewlift.uimodule.schedule.ui.component.text


import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

sealed class CustomCalenderTextSize(val size: TextUnit) {
    object Title : CustomCalenderTextSize(20.sp)
    object SubTitle : CustomCalenderTextSize(16.sp)
    object Normal : CustomCalenderTextSize(12.sp)
    data class Custom(val textUnit: TextUnit) : CustomCalenderTextSize(textUnit)
}
