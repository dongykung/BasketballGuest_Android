package com.dkproject.data.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dkproject.data.data.local.ChatDao
import com.dkproject.domain.model.Chat.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class ChatPagingSource @Inject constructor(
    private val dao: ChatDao,
    private val chatRoomId: String,
    private val date: Long = Date().time
): PagingSource<Int, Chat>() {
    override fun getRefreshKey(state: PagingState<Int, Chat>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chat> {
        val page:Int = params.key?: 0
        val loadSize = params.loadSize
        Log.d("ChatPagingSource", "pageNumber: ${page}")
        return try {
            val chats = withContext(context = Dispatchers.IO) {
                dao.getChatsByChatRoomId(chatRoomId = chatRoomId, loadSize = loadSize, index = page, date = date).map {
                    it.toDomain()
                }
            }
            Log.d("ChatPagingSource", "load: ${chats.size}")
            val nextKey = if (chats.size == params.loadSize) page + 1 else null
            LoadResult.Page(
                data = chats,
                prevKey = if (page == 0) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.d("ChatPagingSource", e.message.toString())
            LoadResult.Error(e)
        }
    }
}