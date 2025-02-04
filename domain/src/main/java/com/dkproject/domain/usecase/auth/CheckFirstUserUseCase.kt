package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class CheckFirstUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(uid: String): Boolean {
        return authRepository.checkUserUid(uid)
    }
}