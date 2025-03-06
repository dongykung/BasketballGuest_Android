package com.dkproject.domain.usecase.MyData

import androidx.paging.PagingData
import com.dkproject.domain.model.Guest.GuestPost
import com.dkproject.domain.repository.MyDataRepository
import kotlinx.coroutines.flow.Flow

class GetMyPostListUseCase(
    private val myDataRepository: MyDataRepository
) {
    operator fun invoke(myUid: String): Flow<PagingData<GuestPost>> {
        return myDataRepository.getMyGuestPostList(myUid = myUid)
    }
}