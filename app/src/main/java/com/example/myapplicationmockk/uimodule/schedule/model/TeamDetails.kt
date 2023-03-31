package com.viewlift.uimodule.schedule.model

data class TeamDetails(
    val homeTeamImage: String?,
    val homeTeamName: String?,
    val homeTeamScore: Int? = null,
    val awayTeamImage: String?,
    val awayTeamName: String?,
    val awayTeamScore: Int? = null
)
