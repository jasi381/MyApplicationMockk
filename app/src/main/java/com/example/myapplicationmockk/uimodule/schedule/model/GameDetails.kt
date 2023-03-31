package com.viewlift.uimodule.schedule.model

data class GameDetails(
    val gameType : String,
    val teamDetails: TeamDetails,
    val gameId: String,
    val broadcaster: String,
    val gameStartTime: Long
)
