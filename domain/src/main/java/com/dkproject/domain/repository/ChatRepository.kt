package com.dkproject.domain.repository

import androidx.paging.PagingData
import com.dkproject.domain.model.Chat.Chat
import com.dkproject.domain.model.UnitResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatsByChatRoomId(chatRoomId: String):  Flow<PagingData<Chat>>
    fun listenToChats(myUid: String, chatRoomId: String): Flow<Unit>
    suspend fun sendMessage(chatRoomId: String, chat: Chat): UnitResult
}