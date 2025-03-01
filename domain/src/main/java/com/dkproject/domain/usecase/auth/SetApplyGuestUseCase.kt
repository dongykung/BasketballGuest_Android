package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class SetApplyGuestUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(myUid: String, postUid: String) {
        return authRepository.setApplyGuest(myUid = myUid, postUid = postUid)
    }
}