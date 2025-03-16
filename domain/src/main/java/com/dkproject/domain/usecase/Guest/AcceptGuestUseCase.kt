package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.repository.GuestRepository

class AcceptGuestUseCase(
    private val guestRepository: GuestRepository
) {
    suspend operator fun invoke(postUid: String, userUid: String): Result<Unit> {
        return guestRepository.acceptGuestUser(postUid = postUid, userUid = userUid)
    }
}