package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.model.Chat.Chat
import com.dkproject.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetLatestMessageUseCase(
    private val chatRepository: ChatRepository
)  {
   operator fun invoke(chatRoomId: String): Flow<List<Chat>> {
       return chatRepository.getLatestMessageFlow(chatRoomId = chatRoomId)
   }
}