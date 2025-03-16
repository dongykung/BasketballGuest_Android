package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class UpdateUserWeightUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userUid: String, weight: Int?): Result<Unit> {
        return authRepository.updateWeight(userUid, weight)
    }
}