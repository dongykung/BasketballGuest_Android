package com.dkproject.data.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dkproject.data.model.GuestPostDTO
import com.dkproject.data.model.toDomain
import com.dkproject.domain.model.MyParticipant
import com.dkproject.domain.model.fromFirestoreValue
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyParticipantsPagingSource @Inject constructor(
    val firestore: FirebaseFirestore,
    val myUid: String
): PagingSource<DocumentSnapshot, MyParticipant>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, MyParticipant>): DocumentSnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, MyParticipant> {
        try {
            val lastDocument = params.key
            var query = firestore.collection("User").document(myUid).collection("Participants")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(10)
            if(lastDocument != null) {
                query = query.startAfter(lastDocument)
            }
            val snapshots = withContext(context = Dispatchers.IO) {
                query.get().await()
            }
            val result = withContext(context = Dispatchers.IO) {
                snapshots.documents.map { documentSnapshot ->
                    async {
                        val myStatus = documentSnapshot.getString("status") ?: return@async null
                        val userStatus = fromFirestoreValue(myStatus)

                        val postSnapshot = firestore.collection("Guest").document(documentSnapshot.id).get().await()
                        val post = postSnapshot.toObject(GuestPostDTO::class.java) ?: return@async null
                        MyParticipant(post = post.toDomain(), myStatus = userStatus)
                    }
                }.awaitAll().filterNotNull()
            }
            return LoadResult.Page(
                data = result,
                prevKey = null,
                nextKey = if(snapshots.documents.size == 10) snapshots.documents.lastOrNull() else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}