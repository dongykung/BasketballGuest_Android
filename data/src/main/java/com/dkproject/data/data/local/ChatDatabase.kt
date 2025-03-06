package com.dkproject.data.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dkproject.data.model.ChatEntity

@Database(entities = [ChatEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun chatDao(): ChatDao
}