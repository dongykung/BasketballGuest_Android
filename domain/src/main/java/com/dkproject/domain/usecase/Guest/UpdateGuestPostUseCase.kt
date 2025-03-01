package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.repository.GuestRepository

class UpdateGuestPostUseCase(
    private val guestRepository: GuestRepository
) {
    suspend operator fun invoke(postUid: String, guestPost: GuestPost): Result<Unit> {
        return guestRepository.updateGuestPost(postUid, guestPost)
    }
}