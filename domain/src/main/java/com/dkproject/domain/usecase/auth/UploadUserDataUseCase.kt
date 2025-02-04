package com.dkproject.domain.usecase.auth

import com.dkproject.domain.model.User
import com.dkproject.domain.repository.AuthRepository

class UploadUserDataUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> = authRepository.uploadUserData(user = user)
}