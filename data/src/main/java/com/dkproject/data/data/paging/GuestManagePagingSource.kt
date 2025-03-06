package com.dkproject.data.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dkproject.data.model.UserDTO
import com.dkproject.domain.model.GuestManage.GuestManage
import com.dkproject.domain.model.User.UserPostStatus
import com.dkproject.domain.model.User.fromFirestoreValue
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GuestManagePagingSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val postUid: String
) : PagingSource<DocumentSnapshot, GuestManage>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, GuestManage>): DocumentSnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, GuestManage> {
        try {
            val lastDocument = params.key
            var query = firestore.collection("Guest").document(postUid).collection("UserStatus")
                .limit(10)
            if(lastDocument != null) {
                query = query.startAfter(lastDocument)
            }
            val snapshots = withContext(context = Dispatchers.IO) {
                query.get().await()
            }
            val result = withContext(context = Dispatchers.IO) {
                snapshots.documents.map { document ->
                    async {
                        val postStatus = document.toObject(UserPostStatus::class.java) ?: return@async null
                        val userStatus = fromFirestoreValue(postStatus.status)

                        val userDataSnapshot = firestore.collection("User").document(document.id).get().await()

                        val user = userDataSnapshot.toObject(UserDTO::class.java) ?: return@async null
                        GuestManage(user = user.toDomain(), userStatus = userStatus)
                    }
                }.awaitAll().filterNotNull()
            }
            return LoadResult.Page(
                data = result,
                prevKey = null,
                nextKey = if (snapshots.documents.size == 10) snapshots.documents.lastOrNull() else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(Exception(e.message))
        }
    }
}

