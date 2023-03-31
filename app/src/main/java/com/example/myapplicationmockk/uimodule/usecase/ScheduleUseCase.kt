package com.viewlift.uimodule.usecase

import com.viewlift.core.extensions.resultOf
import com.viewlift.network.data.remote.model.request.ScheduleListParams
import com.viewlift.network.data.remote.model.request.VideoDetailsParams
import com.viewlift.network.data.remote.model.response.ScheduleEventsResponse
import com.viewlift.network.data.remote.model.response.VideoEntitlementResponse
import com.viewlift.network.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun interface ScheduleUseCase : (ScheduleListParams) -> Flow<Result<ScheduleEventsResponse?>>


/**
 * Get page
 * @author Anand
 *
 * @param pageRepository
 * @param pageParams
 * @return
 */
fun getScheduleList(
    scheduleListParams: ScheduleListParams,
    scheduleRepository: ScheduleRepository
): Flow<Result<ScheduleEventsResponse?>> = scheduleRepository
    .getScheduleList(scheduleListParams)
    .map {
        resultOf { it }
    }.catch {
        emit(Result.failure(it))
    }