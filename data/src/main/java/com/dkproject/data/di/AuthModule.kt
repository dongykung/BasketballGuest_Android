package com.dkproject.data.di

import com.dkproject.data.Repository.AuthRepositoryImpl
import com.dkproject.domain.repository.AuthRepository
import com.dkproject.domain.usecase.auth.CheckFirstUserUseCase
import com.dkproject.domain.usecase.auth.CheckNicknameUseCase
import com.dkproject.domain.usecase.auth.DeleteMyParticipantUseCase
import com.dkproject.domain.usecase.auth.GetUserDataUseCase
import com.dkproject.domain.usecase.auth.SetApplyGuestUseCase
import com.dkproject.domain.usecase.auth.SetFcmTokenUseCase
import com.dkproject.domain.usecase.auth.SetPermissionGuestUseCase
import com.dkproject.domain.usecase.auth.UploadUserDataUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(ViewModelComponent::class)
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

    @Provides
    fun provideSetApplyGuestUseCase(authRepository: AuthRepository): SetApplyGuestUseCase {
        return SetApplyGuestUseCase(authRepository)
    }

    @Provides
    fun provideDeleteMyParticipantUseCase(authRepository: AuthRepository): DeleteMyParticipantUseCase {
        return DeleteMyParticipantUseCase(authRepository)
    }

    @Provides
    fun provideSetPermissionGuestUseCase(authRepository: AuthRepository): SetPermissionGuestUseCase {
        return SetPermissionGuestUseCase(authRepository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindAuthModule {
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}