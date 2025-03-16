package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.model.Chat.Chat
import com.dkproject.domain.repository.ChatRepository

class SendMessageUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatRoomId: String, chat: Chat): Result<Unit> {
        return chatRepository.sendMessage(chatRoomId = chatRoomId, chat = chat)
    }
}