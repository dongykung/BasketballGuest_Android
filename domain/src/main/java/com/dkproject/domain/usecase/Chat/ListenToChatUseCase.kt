package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ListenToChatUseCase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(myUid: String, chatRoomId: String): Flow<Unit> {
        return chatRepository.listenToChats(myUid = myUid, chatRoomId = chatRoomId)
    }
}