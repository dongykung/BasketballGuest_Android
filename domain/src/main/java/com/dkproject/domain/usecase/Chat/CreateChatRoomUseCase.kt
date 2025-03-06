package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.repository.ChatRoomRepository

class CreateChatRoomUseCase(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(chatRoom: ChatRoom): UnitResult {
        return chatRoomRepository.createChatRoom(chatRoom)
    }
}