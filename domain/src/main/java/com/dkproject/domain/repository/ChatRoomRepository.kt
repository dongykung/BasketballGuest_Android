package com.dkproject.domain.repository

import com.dkproject.domain.model.Chat.ChatRoom
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ChatRoomRepository {
    fun getChatRooms(): Flow<List<ChatRoom>> //채팅방 flow로 가져오기
    suspend fun getChatRoomById(chatRoomId: String): ChatRoom? //채팅방 존재하는지 안하는지 확인

    //firestore 부분
    fun listenToChatRooms(myUid: String): Flow<Unit> //파이어 스토어 리스너 걸기
    suspend fun createChatRoom(chatRoom: ChatRoom): Result<Unit>
    suspend fun updateChatRoomData(
        myUid: String,
        chatRoomId: String,
        lastMessage: String,
        date: Date
    ): Result<Unit>
    suspend fun getCountUnReadMessage(uid: String, chatRoomId: String, date: Date): Int
}

