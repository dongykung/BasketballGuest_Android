package com.dkproject.domain.repository

import androidx.paging.PagingData
import com.dkproject.domain.model.Poi
import kotlinx.coroutines.flow.Flow

interface SearchPlaceRepository {
    suspend fun getSearchResult(query: String): Flow<PagingData<Poi>>
}