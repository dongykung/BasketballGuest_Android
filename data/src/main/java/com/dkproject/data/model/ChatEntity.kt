package com.dkproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dkproject.domain.model.Chat.Chat
import java.util.Date

@Entity(tableName = "Chat")
data class ChatEntity(
    @PrimaryKey
    val id: String,
    val chatRoomId: String, // 채팅방 식별용 외래키
    val message: String,
    val sender: String,
    val readBy: List<String>,
    val createAt: Date

) {
    fun toDomain(): Chat {
        return Chat(
            id = id,
            message = message,
            sender = sender,
            readBy = readBy,
            createAt = createAt
        )
    }
}