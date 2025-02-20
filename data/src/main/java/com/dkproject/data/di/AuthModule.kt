package com.dkproject.data.di

import com.dkproject.data.Repository.AuthRepositoryImpl
import com.dkproject.domain.repository.AuthRepository
import com.dkproject.domain.usecase.auth.CheckFirstUserUseCase
import com.dkproject.domain.usecase.auth.CheckNicknameUseCase
import com.dkproject.domain.usecase.auth.GetUserDataUseCase
import com.dkproject.domain.usecase.auth.SetFcmTokenUseCase
import com.dkproject.domain.usecase.auth.UploadUserDataUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object ProvideAuthModule {

    @Provides
    fun provideCheckFirstUserUseCase(authRepository: AuthRepository): CheckFirstUserUseCase {
        return CheckFirstUserUseCase(authRepository)
    }

    @Provides
    fun provideCheckUserNickname(authRepository: AuthRepository): CheckNicknameUseCase {
        return CheckNicknameUseCase(authRepository)
    }

    @Provides
    fun provideUploadUserDataUseCase(authRepository: AuthRepository): UploadUserDataUseCase {
        return UploadUserDataUseCase(authRepository)
    }

    @Provides
    fun provideGetUserDataUseCase(authRepository: AuthRepository): GetUserDataUseCase {
        return GetUserDataUseCase(authRepository)
    }

    @Provides
    fun provideSetFcmTokenUseCase(authRepository: AuthRepository): SetFcmTokenUseCase {
        return SetFcmTokenUseCase(authRepository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindAuthModule {
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}