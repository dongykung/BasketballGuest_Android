package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class SetFcmTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userUid: String) {
        authRepository.setFcmToken(userUid)
    }
}