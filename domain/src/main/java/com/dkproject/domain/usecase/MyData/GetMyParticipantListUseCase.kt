package com.dkproject.domain.usecase.MyData

import androidx.paging.PagingData
import com.dkproject.domain.model.MyData.MyParticipant
import com.dkproject.domain.repository.MyDataRepository
import kotlinx.coroutines.flow.Flow

class GetMyParticipantListUseCase(
    private val myDataRepository: MyDataRepository
) {
    operator fun invoke(myUid: String): Flow<PagingData<MyParticipant>> {
        return myDataRepository.getMyParticipantList(myUid = myUid)
    }
}