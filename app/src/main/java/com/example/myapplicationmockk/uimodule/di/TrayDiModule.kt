package com.viewlift.uimodule.di

import com.viewlift.network.domain.repository.GameDetailsRepository
import com.viewlift.network.domain.repository.ScheduleRepository
import com.viewlift.network.domain.repository.VideoDetailsRepository
import com.viewlift.uimodule.uistate.RefreshUiState
import com.viewlift.uimodule.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object TrayDiModule {

    @Provides
    fun provideVideoRefreshUseCase(
        authRepository: VideoDetailsRepository
    ): VideoRefreshUseCase {
        return VideoRefreshUseCase {
            getEntitlementVideo(it, authRepository)
        }
    }


    @Provides
    fun provideScheduleModuleUseCase(
        scheduleRepository: ScheduleRepository
    ): ScheduleUseCase {
        return ScheduleUseCase {
            getScheduleList(it, scheduleRepository)
        }
    }

    @Provides
    fun provideGameDetailsModuleUseCase(
        gameDetailsRepository: GameDetailsRepository
    ): GameUseCase {
        return GameUseCase {
            getGameDetails(it, gameDetailsRepository)
        }
    }


    @Provides
    fun provideRefreshVideoUiState(): RefreshUiState = RefreshUiState(false, false,null,null, null)


}