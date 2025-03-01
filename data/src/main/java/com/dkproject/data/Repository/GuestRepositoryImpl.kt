package com.dkproject.data.Repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dkproject.data.R
import com.dkproject.data.data.paging.GuestManagePagingSource
import com.dkproject.data.data.paging.GuestPagingSource
import com.dkproject.data.model.GuestPostDTO
import com.dkproject.data.model.toDTO
import com.dkproject.data.model.toDomain
import com.dkproject.domain.Error.ErrorType
import com.dkproject.domain.model.GuestFilter
import com.dkproject.domain.model.GuestManage
import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.model.UserPostStatus
import com.dkproject.domain.model.UserStatus
import com.dkproject.domain.model.fromFirestoreValue
import com.dkproject.domain.repository.GuestRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.tasks.await
import okio.IOException
import javax.inject.Inject

class GuestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : GuestRepository {
    override suspend fun uploadGuestPost(guestPost: GuestPost): Result<Unit> = kotlin.runCatching {
        firestore.collection("Guest").add(guestPost.toDTO()).await()
    }

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

    override suspend fun getPostUserStatus(postUid: String, userUid: String): UserStatus {
        try {
            val snapshot = firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).get().await()
            if (!snapshot.exists()) {
                return UserStatus.NONE
            }
            val status = snapshot.toObject(UserPostStatus::class.java)
                ?: throw Exception()
            return fromFirestoreValue(status.status)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun applyGuestPost(postUid: String, userUid: String) {
        try {
            val data = UserPostStatus("APPLY")
            firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).set(data).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun cancelGuestPost(postUid: String, userUid: String) {
        try {
            firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPostData(postUid: String): GuestPost {
        try {
            val snapshot = firestore.collection("Guest").document(postUid).get().await()
            if (!snapshot.exists()) throw NoSuchElementException()
            return snapshot.toObject(GuestPostDTO::class.java)?.toDomain() ?: throw Exception()
        } catch (e: Exception) {
            throw e
        }
    }

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

    override suspend fun acceptGuestUser(postUid: String, userUid: String): UnitResult {
        return try {
            val updateData = UserStatus.GUEST.name
            firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).update("status", updateData).await()
            UnitResult.Success
        } catch (e: FirebaseFirestoreException) {
            if(e.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                UnitResult.Error(ErrorType.DOCUMENT_NOT_FOUND)
            }
            else UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        } catch (e: Exception) {
            UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun rejectGuestUser(postUid: String, userUid: String): UnitResult {
        return try {
            val updateData = UserStatus.DENIED.name
            firestore.collection("Guest").document(postUid).collection("UserStatus")
                .document(userUid).update("status", updateData).await()
            UnitResult.Success
        } catch (e: FirebaseFirestoreException) {
            if(e.code == FirebaseFirestoreException.Code.NOT_FOUND) UnitResult.Error(ErrorType.DOCUMENT_NOT_FOUND)
            else UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
        catch (e: Exception) {
            UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateGuestPost(postUid: String, guestPost: GuestPost): Result<Unit> =
        runCatching {
            firestore.collection("Guest").document(postUid)
                .set(guestPost.toDTO(), SetOptions.merge()).await()
        }

    override suspend fun deleteGuestPost(postUid: String): Result<Unit> = runCatching {
        firestore.collection("Guest").document(postUid).delete().await()
    }
}