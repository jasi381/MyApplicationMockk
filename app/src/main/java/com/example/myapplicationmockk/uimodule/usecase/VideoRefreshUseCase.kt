package com.viewlift.uimodule.usecase

import com.viewlift.core.extensions.resultOf
import com.viewlift.network.data.remote.model.request.VideoDetailsParams
import com.viewlift.network.data.remote.model.response.VideoEntitlementResponse
import com.viewlift.network.domain.repository.VideoDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun interface VideoRefreshUseCase : (VideoDetailsParams) -> Flow<Result<VideoEntitlementResponse?>>


/**
 * Get page
 * @author Anand
 *
 * @param pageRepository
 * @param pageParams
 * @return
 */
fun getEntitlementVideo(
    videoDetailsParams: VideoDetailsParams,
    videoDetailsRepository: VideoDetailsRepository
): Flow<Result<VideoEntitlementResponse?>> = videoDetailsRepository
    .getEntitlementVideo(videoDetailsParams)
    .map {
        resultOf { it }
    }.catch {
        emit(Result.failure(it))
    }