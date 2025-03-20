package com.dkproject.domain.usecase.Chat

import androidx.paging.PagingData
import com.dkproject.domain.model.Chat.Chat
import com.dkproject.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetChatListUseCase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatRoomId: String): Flow<PagingData<Chat>> {
        return chatRepository.getChatsByChatRoomId(chatRoomId = chatRoomId)
    }
}