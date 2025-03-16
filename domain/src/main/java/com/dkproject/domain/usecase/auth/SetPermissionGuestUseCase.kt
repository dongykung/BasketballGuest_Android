package com.dkproject.domain.usecase.auth

import com.dkproject.domain.repository.AuthRepository

class SetPermissionGuestUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(myUid: String, postUid: String): Result<Unit> {
        return authRepository.setPermissionGuest(myUid = myUid, postUid = postUid)
    }
}