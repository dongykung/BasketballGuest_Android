package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow

class GetChatRoomListUseCase(
    private val chatRoomRepository: ChatRoomRepository
) {
    operator fun invoke(): Flow<List<ChatRoom>> {
        return chatRoomRepository.getChatRooms()
    }
}