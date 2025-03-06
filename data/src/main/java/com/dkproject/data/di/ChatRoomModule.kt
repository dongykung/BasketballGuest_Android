package com.dkproject.data.di

import com.dkproject.data.Repository.ChatRepositoryImpl
import com.dkproject.data.Repository.ChatRoomRepositoryImpl
import com.dkproject.domain.repository.ChatRepository
import com.dkproject.domain.repository.ChatRoomRepository
import com.dkproject.domain.usecase.Chat.CreateChatRoomUseCase
import com.dkproject.domain.usecase.Chat.GetChatListUseCase
import com.dkproject.domain.usecase.Chat.GetChatRoomInfoUseCase
import com.dkproject.domain.usecase.Chat.GetChatRoomListUseCase
import com.dkproject.domain.usecase.Chat.GetCountUnReadMessageUseCase
import com.dkproject.domain.usecase.Chat.ListenToChatRoomUseCase
import com.dkproject.domain.usecase.Chat.ListenToChatUseCase
import com.dkproject.domain.usecase.Chat.SendMessageUseCase
import com.dkproject.domain.usecase.Chat.UpdateChatRoomInfoUseCase
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindChatRoomModule {
    @Binds
    abstract fun bindChatRoomRepository(chatRoomRepositoryImpl: ChatRoomRepositoryImpl): ChatRoomRepository

    @Binds
    abstract fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ProvideChatRoomModule {
    @Provides
    @Singleton // firestore 채팅방 스냅샷
    fun provideListenToChatRoomUseCase(chatRoomRepository: ChatRoomRepository): ListenToChatRoomUseCase {
        return ListenToChatRoomUseCase(chatRoomRepository)
    }

    @Provides
    @Singleton // 채팅방 리스트 가져오기
    fun provideGetChatRoomListUseCase(chatRoomRepository: ChatRoomRepository): GetChatRoomListUseCase {
        return GetChatRoomListUseCase(chatRoomRepository)
    }

    @Provides
    @Singleton // 채팅방 정보 확인하기(채팅방 존재여부)
    fun provideGetChatRoomInfoUseCase(chatRoomRepository: ChatRoomRepository): GetChatRoomInfoUseCase {
        return GetChatRoomInfoUseCase(chatRoomRepository)
    }

    @Provides
    @Singleton // 채팅 리스트 가져오기
    fun provideGetChatListUseCase(chatRepository: ChatRepository): GetChatListUseCase {
        return GetChatListUseCase(chatRepository)
    }

    @Provides
    @Singleton
    fun provideCreateChatRoomUseCase(chatRoomRepository: ChatRoomRepository): CreateChatRoomUseCase {
        return CreateChatRoomUseCase(chatRoomRepository)
    }

    @Provides
    @Singleton
    fun provideListenToChatUseCase(chatRepository: ChatRepository): ListenToChatUseCase {
        return ListenToChatUseCase(chatRepository)
    }

    @Provides
    @Singleton
    fun provideSendMessageUseCase(chatRepository: ChatRepository): SendMessageUseCase {
        return SendMessageUseCase(chatRepository)
    }


    @Provides
    @Singleton
    fun provideUpdateChatRoomInfoUseCase(chatRoomRepository: ChatRoomRepository): UpdateChatRoomInfoUseCase {
        return UpdateChatRoomInfoUseCase(chatRoomRepository)
    }
}