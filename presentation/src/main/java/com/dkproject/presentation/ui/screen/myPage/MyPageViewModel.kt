package com.dkproject.presentation.ui.screen.myPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.presentation.ui.screen.login.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val googleAuthClient: GoogleSignInClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageViewState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MyPageUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: MyPageUiEvent) {
        viewModelScope.launch {
            when (event) {
                MyPageUiEvent.HideLogoutConfirmation -> _uiState.update { it.copy(isLogoutDialogVisible = false) }
                MyPageUiEvent.NavigateToLogin -> {}
                MyPageUiEvent.ShowLogoutConfirmation -> _uiState.update { it.copy(isLogoutDialogVisible = true) }
                MyPageUiEvent.LogOut -> {
                    googleAuthClient.signOut()
                    _uiEvent.emit(MyPageUiEvent.NavigateToLogin)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyPageViewModel", "onCleared")
    }
}


sealed class MyPageUiEvent {
    data object ShowLogoutConfirmation : MyPageUiEvent()
    data object HideLogoutConfirmation : MyPageUiEvent()
    data object LogOut : MyPageUiEvent()
    data object NavigateToLogin : MyPageUiEvent()
}

data class MyPageViewState(
    val isLogoutDialogVisible: Boolean = false
)