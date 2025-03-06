package com.dkproject.data.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dkproject.data.model.ChatRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRoom(chatRoom: ChatRoomEntity)

    @Update
    suspend fun updateChatRoom(chatRoom: ChatRoomEntity)

    @Delete
    suspend fun deleteChatRoom(chatRoom: ChatRoomEntity)

    @Query("SELECT * FROM ChatRoom WHERE id = :chatRoomId")
    suspend fun getChatRoomById(chatRoomId: String): ChatRoomEntity?


    @Query("SELECT * FROM ChatRoom ORDER BY lastMessageAt DESC")
    fun getChatRooms(): Flow<List<ChatRoomEntity>>

}