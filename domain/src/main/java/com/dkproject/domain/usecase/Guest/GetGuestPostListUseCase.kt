package com.dkproject.domain.usecase.Guest

import androidx.paging.PagingData
import com.dkproject.domain.model.GuestFilter
import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.repository.GuestRepository
import kotlinx.coroutines.flow.Flow

class GetGuestPostListUseCase(
    private val guestRepository: GuestRepository
) {
    operator fun invoke(guestFilter: GuestFilter): Flow<PagingData<GuestPost>> {
        return guestRepository.getGuestPostList(guestFilter = guestFilter)
    }
}