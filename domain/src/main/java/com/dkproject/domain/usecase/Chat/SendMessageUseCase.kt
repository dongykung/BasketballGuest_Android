package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.model.Chat.Chat
import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.repository.ChatRepository

class SendMessageUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatRoomId: String, chat: Chat): UnitResult {
        return chatRepository.sendMessage(chatRoomId = chatRoomId, chat = chat)
    }
}