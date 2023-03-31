package com.viewlift.uimodule.uistate

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.viewlift.common.model.ApolloApiError
import com.viewlift.network.data.remote.model.response.GameDetailResponse
import com.viewlift.network.data.remote.model.response.ScheduleEventsResponse
import com.viewlift.network.data.remote.model.response.VideoEntitlementResponse
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class RefreshUiState(
    val isLoading: Boolean = false,
    var isError: Boolean = false,
    @IgnoredOnParcel var refreshUIstate: VideoEntitlementResponse? = null,
    @IgnoredOnParcel var scheduleList: ScheduleEventsResponse? = null,
    @IgnoredOnParcel var gameDetailsResponse: GameDetailResponse? = null,
    @IgnoredOnParcel val apolloApiError: ApolloApiError? = null,
    @IgnoredOnParcel val error: Throwable? = null
) : Parcelable {

    sealed class RefreshUiStatePartialState {
        object Loading : RefreshUiStatePartialState() // for simplicity: initial loading & refreshing

        /**
         * Fetched
         *
         * @property refreshUIstate
         * @constructor Create empty Fetched
         */
        data class Fetched(val refreshUIstate: VideoEntitlementResponse?) : RefreshUiStatePartialState()


        data class FetchedScheduleList(val scheduleListParams: ScheduleEventsResponse?) : RefreshUiStatePartialState()

        data class FetchedGameDetails(val gameDetailsResponse: GameDetailResponse?) : RefreshUiStatePartialState()
        /**
         * Error
         *
         * @property throwable
         * @constructor Create empty Error
         */
        data class Error(val throwable: Throwable) : RefreshUiStatePartialState()
    }

}