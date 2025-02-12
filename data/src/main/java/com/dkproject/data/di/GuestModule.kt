package com.dkproject.data.di

import com.dkproject.data.Repository.GuestRepositoryImpl
import com.dkproject.data.Repository.SearchPlaceRepositoryImpl
import com.dkproject.domain.repository.GuestRepository
import com.dkproject.domain.repository.SearchPlaceRepository
import com.dkproject.domain.usecase.Guest.GetGuestPostListUseCase
import com.dkproject.domain.usecase.Guest.UploadGuestPostUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object GuestUseCaseModule {
    @Provides
    fun provideUploadGuestPostUseCase(guestRepository: GuestRepository): UploadGuestPostUseCase {
        return UploadGuestPostUseCase(guestRepository)
    }

    @Provides
    fun provideGetGuestPostListUseCase(guestRepository: GuestRepository): GetGuestPostListUseCase {
        return GetGuestPostListUseCase(guestRepository)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class GuestRepositoryModule {
    @Binds
    abstract fun bindGuestRepository(guestRepositoryImpl: GuestRepositoryImpl): GuestRepository
}