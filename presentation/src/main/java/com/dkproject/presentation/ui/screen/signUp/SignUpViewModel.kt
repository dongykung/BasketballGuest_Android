package com.dkproject.presentation.ui.screen.signUp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.User.User
import com.dkproject.domain.usecase.auth.CheckNicknameUseCase
import com.dkproject.domain.usecase.auth.SetFcmTokenUseCase
import com.dkproject.domain.usecase.auth.UploadUserDataUseCase
import com.dkproject.presentation.R
import com.dkproject.presentation.model.Position
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val auth: FirebaseAuth,
    private val checkNicknameUseCase: CheckNicknameUseCase,
    private val uploadUserDataUseCase: UploadUserDataUseCase,
    private val setFcmTokenUseCase: SetFcmTokenUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(SignUpViewState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SignUpUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        Log.d("SignUpViewModel", "${auth.currentUser?.uid}")
        _uiState.update { it.copy(user = it.user.copy(id = auth.currentUser?.uid ?: "")) }
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
                uploadUserData()
            }
            SignUpStep.UserInfo -> {
                _uiState.update { it.copy(currentStep = SignUpStep.Position) }
            }
        }
    }

    fun updateNickName(nickName: String) {
        if(nickName.length > 8 || nickName.length < 2) {
            _uiState.update {
                it.copy(user = it.user.copy(nickName = nickName), errorMessage = context.getString(R.string.nicknamelengtherror))
            }
        } else {
            _uiState.update {
                it.copy(user = it.user.copy(nickName = nickName), errorMessage = "")
            }
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

    fun updateUserHeight(height: Int) {
        _uiState.update { it.copy(user = it.user.copy(height = height)) }
    }

    fun updateUserWeight(weight: Int) {
        _uiState.update { it.copy(user = it.user.copy(weight = weight)) }
    }

    fun skipUserInfo() {
        _uiState.update { it.copy(currentStep = SignUpStep.Position, user = it.user.copy(height = null, weight = null)) }
    }

    fun positionToggle(position: Position) {
        val posStr = position.toFirestoreValue()
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

    private fun uploadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = withContext(context = Dispatchers.IO) {
                uploadUserDataUseCase(uiState.value.user)
            }
            setFcmTokenUseCase(uiState.value.user.id)
            result.fold(
                onSuccess = {
                    _uiEvent.emit(SignUpUiEvent.MoveToHome)
                },
                onFailure = {
                    _uiEvent.emit(SignUpUiEvent.ShowSnackbar(context.getString(R.string.failsignup)))
                    _uiState.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SignUpViewModel", "onCleared")
    }
}

sealed class SignUpUiEvent {
    data class ShowSnackbar(val message: String) : SignUpUiEvent()
    data object MoveToHome: SignUpUiEvent()
}

sealed class SignUpStep(val title: Int, val progress: Float) {
    data object Nickname: SignUpStep(R.string.nickname, 0.33f)
    data object UserInfo: SignUpStep(R.string.bodyinfo, 0.66f)
    data object Position: SignUpStep(R.string.userposition, 1.0f)
}

data class SignUpViewState (
    val currentStep: SignUpStep = SignUpStep.Nickname,
    val isLoading: Boolean = false,
    val user: User = User(),
    val errorMessage: String = ""
)