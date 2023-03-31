package com.viewlift.uimodule.intent

import com.viewlift.network.data.remote.model.request.ScheduleListParams
import com.viewlift.network.data.remote.model.request.VideoDetailsParams

sealed class RefreshVideoDataIntent {

    data class GetVideoRefreshContent(val videoDetailsParams: VideoDetailsParams) : RefreshVideoDataIntent()

    data class GetGameDetailsContent(val gameId: String) : RefreshVideoDataIntent()

    data class GetScheduleList(val scheduleListParams: ScheduleListParams) : RefreshVideoDataIntent()
}