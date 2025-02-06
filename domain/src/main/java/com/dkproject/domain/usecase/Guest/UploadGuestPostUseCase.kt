package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.repository.GuestRepository

class UploadGuestPostUseCase(
    private val guestRepository: GuestRepository
) {
    suspend operator fun invoke(guestPost: GuestPost): Result<Unit> {
        return guestRepository.uploadGuestPost(guestPost)
    }
}