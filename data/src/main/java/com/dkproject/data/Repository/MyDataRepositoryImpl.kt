package com.dkproject.data.Repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dkproject.data.data.paging.MyParticipantsPagingSource
import com.dkproject.data.data.paging.MyPostPagingSource
import com.dkproject.data.model.toDomain
import com.dkproject.domain.model.Guest.GuestPost
import com.dkproject.domain.model.MyData.MyParticipant
import com.dkproject.domain.repository.MyDataRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyDataRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    ): MyDataRepository {
    override fun getMyGuestPostList(myUid: String): Flow<PagingData<GuestPost>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1),
            pagingSourceFactory = {
                MyPostPagingSource(firestore = firestore, myUid = myUid)
            }
        ).flow
            .map { it.map { dto -> dto.toDomain() } }
    }

    override fun getMyParticipantList(myUid: String): Flow<PagingData<MyParticipant>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 1),
            pagingSourceFactory = {
                MyParticipantsPagingSource(firestore = firestore, myUid = myUid)
            }
        ).flow
    }
}