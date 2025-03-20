package com.dkproject.presentation.ui.screen.Chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dkproject.domain.model.Chat.Chat
import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.domain.model.Chat.ChatUserInfo
import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.model.User.User
import com.dkproject.domain.usecase.Chat.CreateChatRoomUseCase
import com.dkproject.domain.usecase.Chat.GetChatListUseCase
import com.dkproject.domain.usecase.Chat.GetChatRoomInfoUseCase
import com.dkproject.domain.usecase.Chat.GetCountUnReadMessageUseCase
import com.dkproject.domain.usecase.Chat.ListenToChatUseCase
import com.dkproject.domain.usecase.Chat.SendMessageUseCase
import com.dkproject.domain.usecase.Chat.UpdateChatRoomInfoUseCase
import com.dkproject.domain.usecase.auth.GetUserDataUseCase
import com.dkproject.presentation.R
import com.dkproject.presentation.di.ResourceProvider
import com.dkproject.presentation.navigation.Screen
import com.dkproject.presentation.ui.screen.ChatRoom.ChatRoomUiEvent
import com.dkproject.presentation.ui.screen.GuestDetail.DetailUiEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val resourceProvider: ResourceProvider,
    private val getChatRoomInfoUseCase: GetChatRoomInfoUseCase,
    private val createChatRoomUseCase: CreateChatRoomUseCase,
    private val getChatListUseCase: GetChatListUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val listenToChatUseCase: ListenToChatUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val updateChatRoomInfoUseCase: UpdateChatRoomInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ChatUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // 맨 처음 실행되는 함수(room 으로부터 데이터 가져오기 및 파이어스토어 리스너 달기)
    fun getChatList(chatRoomId: String) {
        _uiState.update { it.copy(chatList = buildChatListFlow(chatRoomId)) }
        startListeningToChats(chatRoomId = chatRoomId)
        initializeChatRoom(chatRoomId = chatRoomId)
    }

    private fun buildChatListFlow(chatRoomId: String): Flow<PagingData<Chat>> {
        return getChatListUseCase(chatRoomId)
            .cachedIn(viewModelScope)
    }

    // 리스너 달기
    private fun startListeningToChats(chatRoomId: String) {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            listenToChatUseCase(myUid = myUid, chatRoomId = chatRoomId)
                .catch {  }
                .collect { ChatRoomUiEvent.ShowToast("채팅 업데이트 실패") }
        }
    }

    //방에 입장했을 때 한 번 호출해야 하는 함수
    private fun initializeChatRoom(chatRoomId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRoomExist = checkRoomExist(chatRoomId = chatRoomId)) }
            if (uiState.value.isRoomExist) { // 채팅방 존재하면 unread한거 read로 가장 마지막 읽은 시점 업데이트 해주기

            }
        }
    }

    private suspend fun checkRoomExist(chatRoomId: String): Boolean {
        val chatRoom = getChatRoomInfoUseCase(chatRoomId = chatRoomId)
        return chatRoom != null
    }

    fun sendMessage(chat: Screen.Chat) {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            val now = Date()
            if (uiState.value.isRoomExist) {
                // 채팅방이 있을 시
                uploadMessage(myUid = myUid, chatRoomId = chat.chatRoomId, now = now)
                updateChatRoomData(chatRoomId = chat.chatRoomId, lastMessage = uiState.value.chatMessage, date = now, myUid = myUid)
                _uiState.update { it.copy(chatMessage = "") }
            } else { // 채팅 방 없을 경우 (채팅방 생성 후 메시지 업로드)
                try {
                    val myData = getUserDataUseCase(userUid = myUid)
                    val chatRoom = getChatRoom(chat = chat, myData = myData, now = now)
                    when (createChatRoomUseCase(chatRoom = chatRoom)) {
                        is UnitResult.Error -> {
                            _uiEvent.emit(ChatUiEvent.ShowToast(resourceProvider.getString(R.string.failcreatechatroom)))
                            return@launch
                        }
                        UnitResult.Success -> {
                            _uiState.update { it.copy(isRoomExist = true) }
                            uploadMessage(myUid = myUid, chatRoomId = chat.chatRoomId, now = now)
                            updateChatRoomData(chatRoomId = chat.chatRoomId, lastMessage = uiState.value.chatMessage, date = now, myUid = myUid)
                            _uiState.update { it.copy(chatMessage = "") }
                        }
                    }
                } catch (e: Exception) {
                    errorHandling(e)
                }
            }
        }
    }

    private fun getChatRoom(chat: Screen.Chat, myData: User, now: Date): ChatRoom {
        return ChatRoom(
            id = chat.chatRoomId,
            lastMessage = uiState.value.chatMessage,
            lastMessageAt = now,
            participant = listOf(myData.id, chat.otherUserUid),
            readStatus = mapOf(myData.id to now, chat.otherUserUid to now),
            participantsInfo = mapOf(
                myData.id to ChatUserInfo(
                    nickname = myData.nickName,
                    profileUrl = myData.profileImageUrl
                ),
                chat.otherUserUid to ChatUserInfo(
                    nickname = chat.otherUserName,
                    profileUrl = chat.otherProfileUrl
                )
            ),
            unReadCount = mapOf(
                myData.id to 0,
                chat.otherUserUid to 0
            )
        )
    }

    private suspend fun uploadMessage(myUid: String, chatRoomId: String, now: Date) {
        val chat = getChat(myUid = myUid, chatRoomId = chatRoomId, now = now)
        when (sendMessageUseCase(chatRoomId = chatRoomId, chat = chat)) {
            is UnitResult.Error -> { _uiEvent.emit(ChatUiEvent.ShowToast(resourceProvider.getString(R.string.failsendmessage))) }
            UnitResult.Success -> {  }
        }
    }

    /**
     * 현재 입력된 메시지로 Chat 데이터 반환하는 함수
     */
    private fun getChat(myUid: String, chatRoomId: String, now: Date): Chat {
        return Chat(
            message = uiState.value.chatMessage,
            sender = myUid,
            readBy = listOf(myUid),
        )
    }

    private fun updateChatRoomData(chatRoomId: String, lastMessage: String, date: Date, myUid: String) {
        viewModelScope.launch {
            updateChatRoomInfoUseCase(myUid, chatRoomId, lastMessage, date)
        }
    }
    fun updateChatMessage(message: String) {
        _uiState.update { it.copy(chatMessage = message) }
    }

    private suspend fun getCurrentUserId(): String? {
        return auth.currentUser?.uid ?: run {
            //_uiEvent.emit(DetailUiEvent.LoseLoginInfo)
            null
        }
    }

    private suspend fun errorHandling(e: Exception) {
        when (e) {
            is NoSuchElementException -> {}
            else -> {}
        }
    }

}

data class ChatUiState(
    val chatList: Flow<PagingData<Chat>> = emptyFlow(),
    val chatMessage: String = "",
    val isRoomExist: Boolean = false,
)

sealed class ChatUiEvent {
    data class ShowToast(val message: String) : ChatUiEvent()
    data object NavigateToLogin : ChatUiEvent()
}
