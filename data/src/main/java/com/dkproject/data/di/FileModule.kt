package com.dkproject.data.di

import com.dkproject.data.Repository.ImageRepositoryImpl
import com.dkproject.domain.repository.ImageRepository
import com.dkproject.domain.usecase.File.UploadProfileImageUseCase
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindFileModule {
    @Binds
    abstract fun bindImageRepository(imageRepositoryImpl: ImageRepositoryImpl): ImageRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProvideFileModule {

    @Provides
    @Singleton
    fun provideUploadProfileImageUseCase(imageRepository: ImageRepository): UploadProfileImageUseCase {
        return UploadProfileImageUseCase(imageRepository)
    }
}