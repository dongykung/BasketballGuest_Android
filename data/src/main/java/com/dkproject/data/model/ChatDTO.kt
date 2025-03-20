package com.dkproject.data.model

import com.dkproject.domain.model.Chat.Chat
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ChatDTO(
    @DocumentId val id: String? = null,
    var message: String = "",
    var sender: String = "",
    var readBy: List<String> = emptyList(),
    @ServerTimestamp var createAt: Date ?= null,
    var allRead:Boolean = false
) {
    fun toChatEntity(chatRoomId: String): ChatEntity {
        return ChatEntity(
            id = id ?: "",
            chatRoomId = chatRoomId,
            message = message,
            sender = sender,
            readBy = readBy,
            createAt = createAt ?: Date(),
            isAllRead = allRead
        )
    }
}

fun Chat.toDTO(): ChatDTO {
    return ChatDTO(
        id = id,
        message = message,
        sender = sender,
        readBy = readBy,
    )
}