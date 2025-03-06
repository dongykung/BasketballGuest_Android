package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.domain.repository.ChatRoomRepository

class GetChatRoomInfoUseCase(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(chatRoomId: String): ChatRoom? {
        return chatRoomRepository.getChatRoomById(chatRoomId)
    }
}