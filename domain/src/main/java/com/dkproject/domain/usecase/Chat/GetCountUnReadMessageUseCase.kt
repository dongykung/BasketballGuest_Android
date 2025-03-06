package com.dkproject.domain.usecase.Chat

import com.dkproject.domain.repository.ChatRoomRepository
import java.util.Date

class GetCountUnReadMessageUseCase(
    private val chatRoomRepository: ChatRoomRepository
) {
    suspend operator fun invoke(uid: String, chatRoomId: String, date: Date): Int {
        return chatRoomRepository.getCountUnReadMessage(uid = uid, chatRoomId = chatRoomId, date = date)
    }
}