package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.repository.ChatRoomRepository
import java.util.Date

class UpdateChatRoomInfoUseCase(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(
        myUid: String,
        chatRoomId: String,
        lastMessage: String,
        date: Date
    ) {
        chatRoomRepository.updateChatRoomData(
            myUid,
            chatRoomId,
            lastMessage,
            date,
        )
    }
}