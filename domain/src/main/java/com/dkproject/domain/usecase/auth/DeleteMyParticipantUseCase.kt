package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class DeleteMyParticipantUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(postUid: String, myUid: String) {
        return authRepository.cancelApplyGuest(postUid = postUid, myUid = myUid)
    }
}