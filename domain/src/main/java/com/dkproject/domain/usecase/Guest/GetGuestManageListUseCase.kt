package com.dkproject.domain.usecase.Guest

import com.dkproject.domain.repository.GuestRepository

class GetGuestManageListUseCase(
    private val guestRepository: GuestRepository
){
    operator fun invoke(postUid: String) = guestRepository.getGuestManageList(postUid)
}