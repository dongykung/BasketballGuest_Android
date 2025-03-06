package com.dkproject.data.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dkproject.data.model.ChatRoomEntity

@Database(entities = [ChatRoomEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ChatRoomDatabase: RoomDatabase() {
    abstract fun chatRoomDao(): ChatRoomDao
}