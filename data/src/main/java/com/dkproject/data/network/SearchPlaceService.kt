package com.dkproject.data.network

import com.dkproject.domain.model.SearchPlace
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchPlaceService {
    @GET("tmap/pois")
    suspend fun getPoi(
        @Query("version") version: Int = 1,
        @Query("page") page: Int,
        @Query("count") count: Int,
        @Query("searchKeyword") searchKeyword: String,
    ): Response<SearchPlace>
}