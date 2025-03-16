package com.dkproject.data.Repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dkproject.data.data.paging.GuestManagePagingSource
import com.dkproject.data.data.paging.GuestPagingSource
import com.dkproject.data.model.GuestPostDTO
import com.dkproject.data.model.UserPostStatusDTO
import com.dkproject.data.model.toDTO
import com.dkproject.data.model.toDomain
import com.dkproject.data.util.mapDomainError
import com.dkproject.domain.model.Guest.GuestFilter
import com.dkproject.domain.model.GuestManage.GuestManage
import com.dkproject.domain.model.Guest.GuestPost
import com.dkproject.domain.model.User.UserPostStatus
import com.dkproject.domain.model.User.UserStatus
import com.dkproject.domain.model.User.fromFirestoreValue
import com.dkproject.domain.repository.GuestRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GuestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : GuestRepository {
    override suspend fun uploadGuestPost(guestPost: GuestPost): Result<Unit> = kotlin.runCatching {
        firestore.collection("Guest").add(guestPost.toDTO()).await()
        Unit
    }.mapDomainError()

    override fun getGuestPostList(guestFilter: GuestFilter): Flow<PagingData<GuestPost>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GuestPagingSource(
                    guestFilter = guestFilter,
                    firestore = firestore,
                    context = context
                )
            }
        ).flow
    }

    override suspend fun getPostUserStatus(postUid: String, userUid: String): Result<UserStatus> =
        kotlin.runCatching {
            val snapshot = firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).get().await()
            if (!snapshot.exists()) {
                return Result.success(UserStatus.NONE)
            }
            val status = snapshot.toObject(UserPostStatusDTO::class.java)
                ?: throw Exception()
            fromFirestoreValue(status.status)
        }.mapDomainError()

    override suspend fun applyGuestPost(postUid: String, userUid: String): Result<Unit> =
        kotlin.runCatching {
            val data = UserPostStatus("APPLY")
            firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).set(data).await()
            Unit
        }.mapDomainError()

    override suspend fun cancelGuestPost(postUid: String, userUid: String): Result<Unit> =
        kotlin.runCatching {
            firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).delete().await()
            Unit
        }.mapDomainError()

    override suspend fun getPostData(postUid: String): Result<GuestPost> = kotlin.runCatching {
        val snapshot = firestore.collection("Guest").document(postUid).get().await()
        if (!snapshot.exists()) throw NoSuchElementException()
        snapshot.toObject(GuestPostDTO::class.java)?.toDomain() ?: throw Exception()
    }.mapDomainError()

    override fun getGuestManageList(postUid: String): Flow<PagingData<GuestManage>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GuestManagePagingSource(
                    firestore = firestore,
                    postUid = postUid
                )
            }
        ).flow
    }

    override suspend fun acceptGuestUser(postUid: String, userUid: String): Result<Unit> =
        runCatching {
            val updateData = UserStatus.GUEST.name
            firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).update("status", updateData).await()
            Unit
        }.mapDomainError()

    override suspend fun rejectGuestUser(postUid: String, userUid: String): Result<Unit> = runCatching {
            val updateData = UserStatus.DENIED.name
            firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).update("status", updateData).await()
            Unit
        }.mapDomainError()


    override suspend fun updateGuestPost(postUid: String, guestPost: GuestPost): Result<Unit> =
        runCatching {
            firestore.collection("Guest").document(postUid)
                .set(guestPost.toDTO(), SetOptions.merge()).await()
            Unit
        }.mapDomainError()

    override suspend fun deleteGuestPost(postUid: String): Result<Unit> = runCatching {
        firestore.collection("Guest").document(postUid).delete().await()
        Unit
    }.mapDomainError()
}