package com.dkproject.domain.repository

import androidx.paging.PagingData
import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.model.MyParticipant
import kotlinx.coroutines.flow.Flow

interface MyDataRepository {
    fun getMyGuestPostList(myUid: String): Flow<PagingData<GuestPost>>
    fun getMyParticipantList(myUid: String): Flow<PagingData<MyParticipant>>
}