package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.repository.GuestRepository

class DeleteGuestPostUseCase(
    private val guestRepository: GuestRepository
) {
    suspend operator fun invoke(postUid: String): Result<Unit> {
        return guestRepository.deleteGuestPost(postUid)
    }
}