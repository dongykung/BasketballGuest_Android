package com.dkproject.data.Repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dkproject.data.data.paging.SearchPlacePagingSource
import com.dkproject.data.data.remote.SearchPlaceDataSource
import com.dkproject.domain.model.Poi
import com.dkproject.domain.repository.SearchPlaceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchPlaceRepositoryImpl @Inject constructor(
    private val searchPlaceDataSource: SearchPlaceDataSource,
    @ApplicationContext private val context: Context
) : SearchPlaceRepository {
    override suspend fun getSearchResult(query: String): Flow<PagingData<Poi>> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { SearchPlacePagingSource(searchPlaceDataSource = searchPlaceDataSource, searchKeyword = query, context = context) }
        ).flow
    }
}