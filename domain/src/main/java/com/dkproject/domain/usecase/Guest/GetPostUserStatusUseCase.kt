package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.model.UserStatus
import com.dkproject.domain.repository.GuestRepository

class GetPostUserStatusUseCase(
    private val guestRepository: GuestRepository
) {
    suspend operator fun invoke(postUid: String, userUid: String): UserStatus {
        return guestRepository.getPostUserStatus(postUid = postUid, userUid = userUid)
    }
}