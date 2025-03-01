package com.dkproject.data.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dkproject.data.model.GuestPostDTO
import com.dkproject.domain.model.GuestPost
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MyPostPagingSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val myUid: String
) : PagingSource<DocumentSnapshot, GuestPostDTO>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, GuestPostDTO>): DocumentSnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, GuestPostDTO> {
        try {
            val lastDocument = params.key
            var query = firestore.collection("Guest").whereEqualTo("writerUid", myUid)
                .orderBy("startDate", Query.Direction.DESCENDING)
                .limit(10)
            if(lastDocument != null) {
                query = query.startAfter(lastDocument)
            }
            val snapshots = withContext(context = Dispatchers.IO) {
                 query.get().await()
            }
            val result = withContext(context = Dispatchers.IO) {
                snapshots.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(GuestPostDTO::class.java)
                }
            }
            return LoadResult.Page(
                data = result,
                prevKey = null,
                nextKey = if (snapshots.documents.size == 10) snapshots.documents.lastOrNull() else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}