package com.viewlift.uimodule.usecase

import com.viewlift.core.extensions.resultOf
import com.viewlift.network.data.remote.model.response.GameDetailResponse
import com.viewlift.network.domain.repository.GameDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun interface GameUseCase : (String) -> Flow<Result<GameDetailResponse?>>


/**
 * Get page
 * @author Anand
 *
 * @param pageRepository
 * @param pageParams
 * @return
 */
fun getGameDetails(
    url: String,
    gameDetailsRepository: GameDetailsRepository
): Flow<Result<GameDetailResponse?>> = gameDetailsRepository
    .getGameDetails(url)
    .map {
        resultOf { it }
    }.catch {
        emit(Result.failure(it))
    }