package com.dkproject.data.Repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dkproject.data.data.paging.GuestPagingSource
import com.dkproject.data.model.toDTO
import com.dkproject.domain.model.GuestFilter
import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.model.UserPostStatus
import com.dkproject.domain.model.UserStatus
import com.dkproject.domain.model.fromFirestoreValue
import com.dkproject.domain.repository.GuestRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GuestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
): GuestRepository {
    override suspend fun uploadGuestPost(guestPost: GuestPost):Result<Unit> = kotlin.runCatching {
        firestore.collection("Guest").add(guestPost.toDTO())
    }

    override fun getGuestPostList(guestFilter: GuestFilter): Flow<PagingData<GuestPost>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {GuestPagingSource(guestFilter = guestFilter, firestore = firestore, context = context) }
        ).flow
    }

    override suspend fun getPostUserStatus(postUid: String, userUid: String): UserStatus {
        try {
            val snapshot = firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).get().await()
            val status = snapshot.toObject(UserPostStatus::class.java)
                ?: throw Exception("User status not found")
            return fromFirestoreValue(status.status)
        } catch (e: Exception) {
            throw e
        }
    }
}