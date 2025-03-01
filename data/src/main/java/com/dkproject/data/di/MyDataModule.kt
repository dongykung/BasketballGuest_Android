package com.dkproject.data.di

import com.dkproject.data.Repository.GuestRepositoryImpl
import com.dkproject.data.Repository.MyDataRepositoryImpl
import com.dkproject.domain.repository.GuestRepository
import com.dkproject.domain.repository.MyDataRepository
import com.dkproject.domain.usecase.MyData.GetMyPostListUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object MyDataModule {

    @Provides
    fun provideGetMyPostListUseCase(myDataRepository: MyDataRepository): GetMyPostListUseCase {
        return GetMyPostListUseCase(myDataRepository = myDataRepository)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class MyDataRepositoryModule {
    @Binds
    abstract fun bindMyDatatRepository(myDataRepositoryImpl: MyDataRepositoryImpl): MyDataRepository
}