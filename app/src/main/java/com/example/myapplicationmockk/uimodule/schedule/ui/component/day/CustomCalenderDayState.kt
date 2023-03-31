package com.viewlift.uimodule.schedule.ui.component.day

sealed interface CustomCalenderDayState {
    object CustomCalenderDaySelected : CustomCalenderDayState
    object CustomCalenderDayDefault : CustomCalenderDayState
}
