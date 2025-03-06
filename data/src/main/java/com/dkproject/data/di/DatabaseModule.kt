package com.dkproject.data.di

import android.content.Context
import androidx.room.Room
import com.dkproject.data.data.local.ChatDao
import com.dkproject.data.data.local.ChatDatabase
import com.dkproject.data.data.local.ChatRoomDao
import com.dkproject.data.data.local.ChatRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideChatRoomDatabase(@ApplicationContext appContext: Context): ChatRoomDatabase {
        return Room.databaseBuilder(
            appContext,
            ChatRoomDatabase::class.java,
            "chat_room_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideChatRoomDao(database: ChatRoomDatabase): ChatRoomDao {
        return database.chatRoomDao()
    }

    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext appContext: Context): ChatDatabase {
        return Room.databaseBuilder(
            appContext,
            ChatDatabase::class.java,
            "chat_database"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideChatDao(database: ChatDatabase): ChatDao {
        return database.chatDao()
    }
}