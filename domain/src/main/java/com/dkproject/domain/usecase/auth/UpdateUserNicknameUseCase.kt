package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class UpdateUserNicknameUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userUid: String, nickname: String): Result<Unit> {
        return authRepository.updateNickname(userUid, nickname)
    }
}