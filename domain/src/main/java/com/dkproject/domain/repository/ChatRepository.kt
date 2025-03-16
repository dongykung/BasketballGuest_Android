package com.dkproject.domain.repository

import com.dkproject.domain.model.Chat.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatsByChatRoomId(chatRoomId: String): Flow<List<Chat>>
    fun listenToChats(myUid: String, chatRoomId: String): Flow<Unit>
    suspend fun sendMessage(chatRoomId: String, chat: Chat): Result<Unit>
}