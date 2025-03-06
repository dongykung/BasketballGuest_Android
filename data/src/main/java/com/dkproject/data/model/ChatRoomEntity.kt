package com.dkproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dkproject.domain.model.Chat.ChatRoom
import java.util.Date

@Entity(tableName = "ChatRoom")
data class ChatRoomEntity(
    @PrimaryKey
    val id: String,
    val lastMessage: String,
    val lastMessageAt: Date,
    val participant: List<String>,
    val readStatus: Map<String, Date>,
    val participantsInfo: Map<String, ChatUserInfoEntity>,
    var unReadCount: Map<String, Int>
) {
    fun toDomain(): ChatRoom {
        return ChatRoom(
            id = id,
            lastMessage = lastMessage,
            lastMessageAt = lastMessageAt,
            participant = participant,
            readStatus = readStatus,
            participantsInfo = participantsInfo.mapValues { it.value.toDomain() },
            unReadCount = unReadCount
        )
    }
}

fun ChatRoom.toEntity(): ChatRoomEntity {
    return ChatRoomEntity(
        id = id,
        lastMessage = lastMessage,
        lastMessageAt = lastMessageAt,
        participant = participant,
        readStatus = readStatus,
        participantsInfo = participantsInfo.mapValues { it.value.toEntity() },
        unReadCount = unReadCount
    )
}

