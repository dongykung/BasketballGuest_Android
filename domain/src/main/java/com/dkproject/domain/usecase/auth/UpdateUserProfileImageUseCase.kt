package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class UpdateUserProfileImageUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userUid: String, photoUri: String): Result<Unit> {
        return authRepository.updateProfileImage(userUid = userUid, photoUri = photoUri)
    }
}