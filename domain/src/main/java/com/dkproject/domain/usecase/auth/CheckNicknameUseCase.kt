package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

/**
 * true -> 닉네임 사용 가능
 */
class CheckNicknameUseCase(
    private val authRepository: AuthRepository
) {
    /// true -> 닉네임 사용 가능
    suspend operator fun invoke(nickName: String): Result<Boolean> = kotlin.runCatching {
        authRepository.checkUserNickName(nickName)
    }
}