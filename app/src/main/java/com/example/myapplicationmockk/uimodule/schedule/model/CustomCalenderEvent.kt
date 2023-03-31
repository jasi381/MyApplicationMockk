package com.viewlift.uimodule.schedule.model


import kotlinx.datetime.LocalDate


data class CustomCalenderEvent(
    val date: LocalDate,
    val eventName: String,
    val eventDescription: String? = null,
)
