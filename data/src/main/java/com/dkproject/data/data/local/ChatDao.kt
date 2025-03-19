package com.dkproject.data.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dkproject.data.model.ChatEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ChatDao {
    // 키셋 페이징: createAt를 기준으로 쿼리하는 키셋 페이징 방식
    @Query("SELECT * FROM Chat WHERE chatRoomId = :chatRoomId ORDER BY createAt DESC LIMIT :loadSize OFFSET (:index * :loadSize)")
    fun getChatsByChatRoomId(chatRoomId: String, loadSize: Int = 20, index: Int): List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Query("SELECT * FROM Chat WHERE chatRoomId = :chatRoomId AND createAt > :lastFetched ORDER BY createAt DESC")
    fun getLatestMessageFlow(chatRoomId: String, lastFetched: Long = Date().time): Flow<List<ChatEntity>>
}