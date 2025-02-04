package com.dkproject.presentation.ui.screen.signUp

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.User
import com.dkproject.domain.usecase.auth.CheckNicknameUseCase
import com.dkproject.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val checkNicknameUseCase: CheckNicknameUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(SignUpViewState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SignUpUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        Log.d("SignUpViewModel", "init")
    }

    fun onBackClick() {
        when(uiState.value.currentStep) {
            SignUpStep.Position -> _uiState.update { it.copy(currentStep = SignUpStep.UserInfo) }
            SignUpStep.UserInfo -> _uiState.update { it.copy(currentStep = SignUpStep.Nickname) }
            else -> {}
        }
    }


    fun nextStep() {
        when(uiState.value.currentStep) {
            SignUpStep.Nickname -> {
                checkNickname(uiState.value.user.nickName)
            }
            SignUpStep.Position -> {

            }
            SignUpStep.UserInfo -> {
                _uiState.update { it.copy(currentStep = SignUpStep.Position) }
            }
        }
    }

    fun updateNickName(nickName: String) {
        _uiState.update {
            it.copy(user = it.user.copy(nickName = nickName))
        }
    }

    private fun checkNickname(nickName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            checkNicknameUseCase(nickName = nickName).fold(
                onSuccess = { result ->
                    if (result) {
                        _uiState.update { it.copy(currentStep = SignUpStep.UserInfo) }
                    } else {
                        _uiEvent.emit(SignUpUiEvent.ShowSnackbar(context.getString(R.string.existnickname)))
                    }
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = {
                    _uiEvent.emit(SignUpUiEvent.ShowSnackbar(context.getString(R.string.failchecknickname)))
                    _uiState.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    fun positionToggle(position: Position) {
        val posStr = position.toString()
        val currentPositions = uiState.value.user.position
        val updatedPositions = if (currentPositions.contains(posStr)) {
            currentPositions - posStr
        } else {
            currentPositions + posStr
        }
        _uiState.update { state ->
            state.copy(user = state.user.copy(position = updatedPositions))
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SignUpViewModel", "onCleared")
    }
}

sealed class SignUpUiEvent {
    data class ShowSnackbar(val message: String) : SignUpUiEvent()
}

sealed class SignUpStep(val title: Int, val progress: Float) {
    data object Nickname: SignUpStep(R.string.nickname, 0.33f)
    data object UserInfo: SignUpStep(R.string.bodyinfo, 0.66f)
    data object Position: SignUpStep(R.string.userposition, 1.0f)
}

data class SignUpViewState (
    val currentStep: SignUpStep = SignUpStep.Nickname,
    val isLoading: Boolean = false,
    val user: User = User()
)