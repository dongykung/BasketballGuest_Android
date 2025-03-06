package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow

class ListenToChatRoomUseCase(
    private val chatRoomRepository: ChatRoomRepository
) {
    operator fun invoke(myUid: String): Flow<Unit> {
        return chatRoomRepository.listenToChatRooms(myUid = myUid)
    }
}