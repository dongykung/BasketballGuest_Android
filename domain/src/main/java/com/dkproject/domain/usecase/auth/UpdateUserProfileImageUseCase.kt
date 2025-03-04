package com.dkproject.domain.usecase.auth

import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.repository.AuthRepository

class UpdateUserProfileImageUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userUid: String, photoUri: String): UnitResult {
        return authRepository.updateProfileImage(userUid = userUid, photoUri = photoUri)
    }
}