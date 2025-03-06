package com.dkproject.data.model

import com.dkproject.data.data.local.ChatRoomDao
import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.domain.model.Chat.ChatUserInfo
import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable
import java.util.Date

data class ChatRoomDTO(
    @DocumentId val id: String? = null,
    /**
     * 마지막 메시지
     */
    val lastMessage: String = "",
    /**
     * 마지막 메시지 날짜
     */
    val lastMessageAt: Date = Date(),
    /**
     * 참여자 uid 목록
     */
    val participant: List<String> = emptyList(),
    /**
     * 참여자 마지막으로 읽은 메시지 날짜
     */
    val readStatus: Map<String, Date> = emptyMap(),
    /**
     * 참여자 정보
     */
    val participantsInfo: Map<String, ChatUserInfoEntity> = emptyMap(),
    /**
     * 참여자 안읽은 메시지 수
     */
    val unReadCount: Map<String, Int> = emptyMap()
) {
    fun toChatRoomEntitiy(): ChatRoomEntity {
        return ChatRoomEntity(
            id = id ?: "",
            lastMessage = lastMessage,
            lastMessageAt = lastMessageAt,
            participant = participant,
            readStatus = readStatus,
            participantsInfo = participantsInfo,
            unReadCount = unReadCount
        )
    }
}

fun ChatRoom.toDTO(): ChatRoomDTO {
    return ChatRoomDTO(
        id = id,
        lastMessage = lastMessage,
        lastMessageAt = lastMessageAt,
        participant = participant,
        readStatus = readStatus,
        participantsInfo = participantsInfo.mapValues { it.value.toEntity() },
        unReadCount = unReadCount
    )
}

@Serializable
data class ChatUserInfoEntity(
    val nickname: String = "",
    val profileUrl: String = "",
    val status: String = "active"
) {
    fun toDomain(): ChatUserInfo {
        return ChatUserInfo(
            nickname = nickname,
            profileUrl = profileUrl,
            status = status
        )
    }
}

fun ChatUserInfo.toEntity(): ChatUserInfoEntity {
    return ChatUserInfoEntity(
        nickname = nickname,
        profileUrl = profileUrl,
        status = status
    )
}