package com.dkproject.domain.usecase.auth

import com.dkproject.domain.model.User.User
import com.dkproject.domain.repository.AuthRepository

class GetUserDataUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userUid: String): User {
        return authRepository.getUserData(userUid = userUid)
    }
}