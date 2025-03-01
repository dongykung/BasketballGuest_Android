package com.dkproject.data.di

import com.dkproject.data.Repository.GuestRepositoryImpl
import com.dkproject.data.Repository.SearchPlaceRepositoryImpl
import com.dkproject.domain.repository.GuestRepository
import com.dkproject.domain.repository.SearchPlaceRepository
import com.dkproject.domain.usecase.Guest.AcceptGuestUseCase
import com.dkproject.domain.usecase.Guest.ApplyGuestUseCase
import com.dkproject.domain.usecase.Guest.CancelGuestUseCase
import com.dkproject.domain.usecase.Guest.DeleteGuestPostUseCase
import com.dkproject.domain.usecase.Guest.GetGuestManageListUseCase
import com.dkproject.domain.usecase.Guest.GetGuestPostListUseCase
import com.dkproject.domain.usecase.Guest.GetPostDataUseCase
import com.dkproject.domain.usecase.Guest.GetPostUserStatusUseCase
import com.dkproject.domain.usecase.Guest.RejectGuestUseCase
import com.dkproject.domain.usecase.Guest.UpdateGuestPostUseCase
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

    @Provides
    fun getPostUserStatusUseCase(guestRepository: GuestRepository): GetPostUserStatusUseCase {
        return GetPostUserStatusUseCase(guestRepository)
    }

    @Provides
    fun applyGuestPostUseCase(guestRepository: GuestRepository): ApplyGuestUseCase {
        return ApplyGuestUseCase(guestRepository)
    }

    @Provides
    fun cancelGuestPostUseCase(guestRepository: GuestRepository): CancelGuestUseCase {
        return CancelGuestUseCase(guestRepository)
    }

    @Provides
    fun provideGetPostDataUseCase(guestRepository: GuestRepository): GetPostDataUseCase {
        return GetPostDataUseCase(guestRepository)
    }

    @Provides
    fun provideGetGuestManageListUseCase(guestRepository: GuestRepository): GetGuestManageListUseCase {
        return GetGuestManageListUseCase(guestRepository)
    }

    @Provides
    fun provideAcceptGuestUserUseCase(guestRepository: GuestRepository): AcceptGuestUseCase {
        return AcceptGuestUseCase(guestRepository)
    }

    @Provides
    fun provideRejectGuestUserUseCase(guestRepository: GuestRepository): RejectGuestUseCase {
        return RejectGuestUseCase(guestRepository)
    }

    @Provides
    fun provideUpdateGuestPostUseCase(guestRepository: GuestRepository): UpdateGuestPostUseCase {
        return UpdateGuestPostUseCase(guestRepository)
    }

    @Provides
    fun provideDeleteGuestPostUseCase(guestRepository: GuestRepository): DeleteGuestPostUseCase {
        return DeleteGuestPostUseCase(guestRepository)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class GuestRepositoryModule {
    @Binds
    abstract fun bindGuestRepository(guestRepositoryImpl: GuestRepositoryImpl): GuestRepository
}