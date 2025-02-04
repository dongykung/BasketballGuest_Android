package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class CheckNicknameUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(nickName: String): Result<Boolean> = kotlin.runCatching {
        authRepository.checkUserNickName(nickName)
    }
}