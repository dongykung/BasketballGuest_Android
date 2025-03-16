package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.repository.GuestRepository

class ApplyGuestUseCase(
    private val guestRepository: GuestRepository
) {
    suspend operator fun invoke(postUid: String, userUid: String): Result<Unit> = guestRepository.applyGuestPost(postUid, userUid)
}