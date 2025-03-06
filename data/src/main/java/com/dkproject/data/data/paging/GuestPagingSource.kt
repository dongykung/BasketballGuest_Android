package com.dkproject.data.data.paging

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dkproject.data.R
import com.dkproject.data.model.GuestPostDTO
import com.dkproject.data.model.toDomain
import com.dkproject.data.util.calculateCoordinateBounds
import com.dkproject.domain.model.Guest.GuestFilter
import com.dkproject.domain.model.Guest.GuestPost
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class GuestPagingSource @Inject constructor(
    private val guestFilter: GuestFilter,
    private val firestore: FirebaseFirestore,
    private val context: Context
): PagingSource<QuerySnapshot, GuestPost>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, GuestPost>): QuerySnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, GuestPost> {
        try {
            val lastDocument: DocumentSnapshot? = params.key?.documents?.lastOrNull()

            val query = buildQuery(guestFilter, lastDocument)

            val snapshot = withContext(Dispatchers.IO) {
                query.get().await()
            }
            val guestPostDTO = snapshot.documents.mapNotNull { it.toObject(GuestPostDTO::class.java) }

            val guestPost = guestPostDTO.map { it.toDomain() }
            Log.d("GuestPagingSource", "${guestPost.size} , ${Thread.currentThread().name}")
            return LoadResult.Page(
                data = guestPost,
                prevKey = null,
                nextKey = if(snapshot.documents.size == 10) snapshot.takeIf { it.documents.isNotEmpty() } else null
            )
        } catch (e: IOException) {
            return LoadResult.Error(Exception(context.getString(R.string.networkerror)))
        } catch (e: Exception) {
            Log.d("GuestPagingSource", "${e.message}")
            return LoadResult.Error(Exception(context.getString(R.string.dataerror)))
        }
    }

    private fun buildQuery(filter: GuestFilter, lastDocument: DocumentSnapshot?): Query {
        var query: Query = firestore.collection("Guest")
            .orderBy("startDate", Query.Direction.ASCENDING)
            .limit(filter.limit.toLong())

        if(filter.isNearBy) {
            filter.myLocation?.let { location ->
                Log.d("GuestPaging", "${location.latitude}, ${location.longitude}")
                val bounds = calculateCoordinateBounds(location, filter.radiusInMeters)
                query = query
                    .whereGreaterThanOrEqualTo("lng", bounds.minLongitude)
                    .whereLessThanOrEqualTo("lng", bounds.maxLongitude)
                    .whereGreaterThanOrEqualTo("lat", bounds.minLatitude)
                    .whereLessThanOrEqualTo("lat", bounds.maxLatitude)
            }
        }

        if(filter.selectedDate != null) {
            query = query.whereEqualTo("date", filter.selectedDate)
        } else {
            query = query.whereGreaterThan("startDate", Date())
        }

        if(filter.selectedPosition.isNotEmpty()) {
            query = query.whereArrayContainsAny("positions", filter.selectedPosition + "무관")
        }

        if(lastDocument != null) {
            query = query.startAfter(lastDocument)
        }
        return query
    }
}