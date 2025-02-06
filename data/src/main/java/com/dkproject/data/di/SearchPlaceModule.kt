package com.dkproject.data.di

import com.dkproject.data.Repository.AuthRepositoryImpl
import com.dkproject.data.Repository.SearchPlaceRepositoryImpl
import com.dkproject.data.data.remote.SearchPlaceDataSource
import com.dkproject.data.data.remote.SearchPlaceRemoteDataSource
import com.dkproject.data.network.SearchPlaceService
import com.dkproject.domain.repository.SearchPlaceRepository
import com.dkproject.domain.usecase.searchPlace.SearchPlaceUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchPlaceModule {
    @Binds
    abstract fun bindSearchPlaceDataSource(searchPlaceRemoteDataSource: SearchPlaceRemoteDataSource): SearchPlaceDataSource
    @Binds
    abstract fun bindSearchPlaceRepository(searchPlaceRepositoryImpl: SearchPlaceRepositoryImpl): SearchPlaceRepository
}

@Module
@InstallIn(ViewModelComponent::class)
object SearchPlaceProvideModule {
    @Provides
    fun provideSearchPlaceUseCase(searchPlaceRepository: SearchPlaceRepository): SearchPlaceUseCase {
        return SearchPlaceUseCase(searchPlaceRepository)
    }
}
