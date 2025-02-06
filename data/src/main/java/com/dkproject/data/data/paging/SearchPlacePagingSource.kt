package com.dkproject.data.data.paging

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dkproject.data.R
import com.dkproject.data.data.remote.SearchPlaceDataSource
import com.dkproject.domain.model.Poi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SearchPlacePagingSource (
    private val searchPlaceDataSource: SearchPlaceDataSource,
    private val searchKeyword: String,
    private val context: Context
): PagingSource<Int, Poi>() {
    override fun getRefreshKey(state: PagingState<Int, Poi>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Poi> {
        try {
            val page:Int = params.key?:1
            val loadSize = params.loadSize

            val response = withContext(context = Dispatchers.IO) {
                searchPlaceDataSource.getPoi(page = page, count = loadSize, searchKeyword = searchKeyword)
            }

            if (response.isSuccessful) {
                val body = response.body()
                val result: List<Poi> = if (body == null || body.searchPoiInfo.pois.poi.isEmpty())
                    emptyList() else body.searchPoiInfo.pois.poi

                Log.d("SearchPlacePagingSource", "$page  ${result.size} " )
                return LoadResult.Page(
                    data = result,
                    prevKey = null,
                    nextKey = if (result.size == params.loadSize) page + 1 else null
                )
            } else {
                val errorCode = response.code()
                val errorMessage = when (errorCode) {
                    403 -> context.getString(R.string.apierror)
                    else -> context.getString(R.string.httperror)
                }
                return LoadResult.Error(Exception(errorMessage))
            }
        } catch (e: IOException) {
            return LoadResult.Error(Exception(context.getString(R.string.networkerror)))
        } catch (e: HttpException) {
            return LoadResult.Error(Exception(context.getString(R.string.httperror)))
        }
    }
}