package com.dkproject.domain.usecase.auth

import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.repository.AuthRepository

class UpdateUserHeightUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userUid: String, height: Int?): UnitResult {
        return authRepository.updateHeight(userUid, height)
    }
}