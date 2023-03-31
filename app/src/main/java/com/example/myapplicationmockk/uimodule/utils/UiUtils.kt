package com.viewlift.uimodule.utils

import com.google.gson.Gson
import com.viewlift.common.label.CarouselLabels
import com.viewlift.common.utils.CommonUtils
import com.viewlift.network.PageQuery
import com.viewlift.network.data.remote.model.response.GameDetailResponse
import com.viewlift.uimodule.data.ContentData
import com.viewlift.uimodule.data.FontsSetting
import com.viewlift.uimodule.data.Layout
import com.viewlift.uimodule.data.Settings
import com.viewlift.uimodule.schedule.model.GameDetails
import com.viewlift.uimodule.viewmodel.GameChangeState
import com.viewlift.uimodule.viewmodel.HomeAwayTeamScore
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UiUtils {

    companion object {

        fun getHomeTeamName(contentData: ContentData?): String {
            val shortName = contentData?.homeTeam?.shortName?.uppercase(Locale.getDefault())
            val longName = contentData?.homeTeam?.gist?.title?.uppercase(Locale.getDefault()) ?: ""

            return shortName ?: longName
        }


        fun getScheduleHomeTeamName(shortName: String?, title: String?): String {
            val shortname01 = shortName?.uppercase(Locale.getDefault())
            val longName = title?.uppercase(Locale.getDefault()) ?: ""

            return shortname01 ?: longName
        }


        fun getAwayTeamName(contentData: ContentData?): String {
            val shortName = contentData?.awayTeam?.shortName?.uppercase(Locale.getDefault())
            val longName = contentData?.awayTeam?.gist?.title?.uppercase(Locale.getDefault()) ?: ""

            return shortName ?: longName
        }

        fun getScheduleAwayTeamName(shortName: String?, title: String?): String {
            val shortname01 = shortName?.uppercase(Locale.getDefault())
            val longName = title?.uppercase(Locale.getDefault()) ?: ""

            return shortname01 ?: longName
        }

        fun getPreNoGameText(timeInMillis: Long): String {
            val liveTimeInMillis = timeInMillis * 1000
            val parserFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = Date(liveTimeInMillis)
            return parserFormat.format(date)
        }

        fun getPublishDate(timeInMillis: Long): String {
            val publishDate = timeInMillis * 1000

            val currentTime = System.currentTimeMillis()

            val differenceInMillis = currentTime - publishDate

            val diffInDays: Long = TimeUnit.MILLISECONDS.toDays(differenceInMillis)

            if (diffInDays == 0L && publishDate < currentTime) {
                // Today
                val hoursDiff: Long = TimeUnit.MILLISECONDS.toHours(differenceInMillis)

                return "${hoursDiff}h"

            } else {
                return "${diffInDays}d"
            }
        }

        fun getGameStartOnlyDate(timeInMillis: Long?): String {
            return if (timeInMillis!=null){
                val liveTimeInMillis = timeInMillis * 1000

                val calender = Calendar.getInstance()
                calender.timeInMillis = liveTimeInMillis

                val simpleDateFormat = SimpleDateFormat("MM/dd hh:mm aa")
                val date = simpleDateFormat.format(calender.getTime())

                "${CarouselLabels.gameStartsAt} $date"
            } else {
                ""
            }
        }

        fun getVideoIdFromGameId(gameDetails: GameDetailResponse?): String?{

            return when (gameDetails?.currentState) {
                "default" -> {
                    return gameDetails.preview?.id
                }
                "pre" -> {
                    return gameDetails.preview?.id
                }
                "live" -> {
                    return if (gameDetails.livestreams.isNotEmpty()){
                        gameDetails.livestreams.first().id
                    } else null
                }
                "post" -> {
                    return if (gameDetails.highlights.isNotEmpty()){
                        gameDetails.highlights.first().id
                    } else null
                }
                else -> null
            }
        }
    }
}