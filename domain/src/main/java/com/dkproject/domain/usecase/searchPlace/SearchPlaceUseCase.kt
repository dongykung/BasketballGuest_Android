package com.dkproject.domain.usecase.searchPlace

import androidx.paging.PagingData
import com.dkproject.domain.model.Poi
import com.dkproject.domain.repository.SearchPlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SearchPlaceUseCase(
    private val searchPlaceRepository: SearchPlaceRepository
) {
    suspend operator fun invoke(query: String): Flow<PagingData<Poi>> {
        return searchPlaceRepository.getSearchResult(query = query)
    }
}