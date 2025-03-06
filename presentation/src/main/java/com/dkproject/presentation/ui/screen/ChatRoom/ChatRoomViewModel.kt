package com.dkproject.presentation.ui.screen.ChatRoom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.domain.usecase.Chat.GetChatRoomListUseCase
import com.dkproject.domain.usecase.Chat.ListenToChatRoomUseCase
import com.dkproject.presentation.R
import com.dkproject.presentation.di.ResourceProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val resourceProvider: ResourceProvider,
    private val listenToChatRoomUseCase: ListenToChatRoomUseCase,
    private val getChatRoomListUseCase: GetChatRoomListUseCase,
): ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    private val _uiEvent = MutableSharedFlow<ChatRoomUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val chatRoomList: StateFlow<List<ChatRoom>> = getChatRoomListUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    init {
        Log.d("ChatRoomViewModel", "startListeningToChatRooms")
        startListeningToChatRooms()
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            _uiEvent.emit( ChatRoomUiEvent.MyUid(myUid))
        }
    }

    private fun startListeningToChatRooms() {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            listenToChatRoomUseCase(myUid = myUid)
                .catch {

                }
                .collect {
                    ChatRoomUiEvent.ShowToast("채팅방 업데이트 실패")
                }
        }
    }

    private suspend fun getCurrentUserId(): String? {
        return auth.currentUser?.uid ?: run {
            _uiEvent.emit(ChatRoomUiEvent.ShowToast(resourceProvider.getString(R.string.loselogin)))
            _uiEvent.emit(ChatRoomUiEvent.NavigateToLogin)
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

sealed class ChatRoomUiEvent {
    data class ShowToast(val message: String) : ChatRoomUiEvent()
    data object NavigateToLogin: ChatRoomUiEvent()
    data class MyUid(val myUid: String): ChatRoomUiEvent()
}