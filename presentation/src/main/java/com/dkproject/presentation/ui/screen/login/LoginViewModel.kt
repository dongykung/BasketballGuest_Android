package com.dkproject.presentation.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.usecase.auth.CheckFirstUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleAuthClient: GoogleSignInClient,
    private val kakaoAuthClient: KakaoSignInClient,
    private val checkFirstUserUseCase: CheckFirstUserUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState: StateFlow<LoginViewState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun signInWithGoogle() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                when (val result = googleAuthClient.signIn()) {

                    is SignInResult.Failure -> {
                        _uiEvent.send(LoginUiEvent.ShowSnackbar(result.errorMessage))
                    }

                    is SignInResult.Success -> {
                        if (checkFirstUserUseCase(result.userUid).getOrThrow()) {
                            _uiEvent.send(LoginUiEvent.MoveToHome)
                        } else {
                            _uiEvent.send(LoginUiEvent.MoveToSignUp)
                        }
                    }
                }
            } catch (e: Throwable) {
                _uiEvent.send(LoginUiEvent.ShowSnackbar("로그인에 실패했습니다 다시 시도해 주세요."))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun signWitnKakako() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                when (val result = kakaoAuthClient.signInWithKakao()) {
                    is SignInResult.Failure -> {
                        _uiEvent.send(LoginUiEvent.ShowSnackbar(result.errorMessage))
                        Log.d("LoginViewModel", result.errorMessage)

                    }

                    is SignInResult.Success -> {
                        if (checkFirstUserUseCase(result.userUid).getOrThrow()) {
                            _uiEvent.send(LoginUiEvent.MoveToHome)
                        } else {
                            _uiEvent.send(LoginUiEvent.MoveToSignUp)
                        }
                    }
                }
            } catch (e: Throwable) {
                _uiEvent.send(LoginUiEvent.ShowSnackbar("로그인에 실패했습니다 다시 시도해 주세요."))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("LoginViewModel", "LoginViewModel cleared")
    }
}

data class LoginViewState(
    val isLoading: Boolean = false,
)

sealed class LoginUiEvent {
    data class ShowSnackbar(val message: String) : LoginUiEvent()
    data object MoveToHome: LoginUiEvent()
    data object MoveToSignUp: LoginUiEvent()
}

sealed class SignInResult {
    data class Success(val userUid: String) : SignInResult()
    data class Failure(val errorMessage: String) : SignInResult()
}