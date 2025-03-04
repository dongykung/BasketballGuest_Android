package com.dkproject.domain.usecase.auth

import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.repository.AuthRepository

class UpdateUserPositionUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userUid: String, position: List<String>): UnitResult {
        return authRepository.updatePosition(userUid, position)
    }
}