package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.model.User.UserStatus
import com.dkproject.domain.repository.GuestRepository

class GetPostUserStatusUseCase(
    private val guestRepository: GuestRepository
) {
    suspend operator fun invoke(postUid: String, userUid: String): Result<UserStatus> = guestRepository.getPostUserStatus(postUid = postUid, userUid = userUid)
}