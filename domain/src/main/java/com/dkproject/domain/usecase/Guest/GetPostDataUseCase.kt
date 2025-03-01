package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.repository.GuestRepository

class GetPostDataUseCase(
    private val guestRepository: GuestRepository
) {
    suspend operator fun invoke(postUid: String): GuestPost {
        return guestRepository.getPostData(postUid = postUid)
    }
}