package com.dkproject.data.data.remote

import com.dkproject.data.network.SearchPlaceService
import com.dkproject.domain.model.Poi
import com.dkproject.domain.model.SearchPlace
import retrofit2.Response
import javax.inject.Inject

interface SearchPlaceDataSource {
    suspend fun getPoi(page: Int, count: Int, searchKeyword: String): Response<SearchPlace>
}

class SearchPlaceRemoteDataSource @Inject constructor(
    private val searchPlaceService: SearchPlaceService
): SearchPlaceDataSource {
    override suspend fun getPoi(page: Int, count: Int, searchKeyword: String): Response<SearchPlace> {
        return searchPlaceService.getPoi(page = page, count = count, searchKeyword = searchKeyword)
    }
}