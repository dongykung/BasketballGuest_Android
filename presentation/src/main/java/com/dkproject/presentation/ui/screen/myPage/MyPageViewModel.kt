package com.dkproject.presentation.ui.screen.myPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.DataState
import com.dkproject.domain.model.User.User
import com.dkproject.domain.usecase.File.UploadProfileImageUseCase
import com.dkproject.domain.usecase.auth.CheckNicknameUseCase
import com.dkproject.domain.usecase.auth.GetUserDataUseCase
import com.dkproject.domain.usecase.auth.UpdateUserHeightUseCase
import com.dkproject.domain.usecase.auth.UpdateUserNicknameUseCase
import com.dkproject.domain.usecase.auth.UpdateUserPositionUseCase
import com.dkproject.domain.usecase.auth.UpdateUserProfileImageUseCase
import com.dkproject.domain.usecase.auth.UpdateUserWeightUseCase
import com.dkproject.presentation.R
import com.dkproject.presentation.di.ResourceProvider
import com.dkproject.presentation.ui.screen.login.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val googleAuthClient: GoogleSignInClient,
    private val auth: FirebaseAuth,
    private val resourceProvider: ResourceProvider,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserPositionUseCase: UpdateUserPositionUseCase,
    private val updateUserHeightUseCase: UpdateUserHeightUseCase,
    private val updateUserWeightUseCase: UpdateUserWeightUseCase,
    private val checkNicknameUseCase: CheckNicknameUseCase,
    private val updateUserNicknameUseCase: UpdateUserNicknameUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val updateUserProfileImageUseCase: UpdateUserProfileImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageViewState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MyPageUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadMyData()
    }

    fun loadMyData() {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            try {
                val result = getUserDataUseCase(userUid = myUid).getOrThrow()
                _uiState.update { it.copy(dataState = DataState.Success(result), isUpdateLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(dataState = DataState.Error(e.message.toString()), isUpdateLoading = false) }
            }
        }
    }

    fun updateUserNickname(name: String) {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            _uiState.update { it.copy(isUpdateLoading = true) }
            try {
                val result = checkNicknameUseCase(nickName = name).getOrThrow()
                if(result) {
                    updateUserNicknameUseCase(userUid = myUid, nickname = name).fold(
                        onSuccess = {
                            _uiEvent.emit(MyPageUiEvent.CompleteChangeNickname)
                            loadMyData()
                        },
                        onFailure = { throw Exception() }
                    )
                } else {
                    _uiState.update { it.copy(isUpdateLoading = false) }
                    _uiEvent.emit(MyPageUiEvent.ShowToast(resourceProvider.getString(R.string.existnickname)))
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isUpdateLoading = false) }
                _uiEvent.emit(MyPageUiEvent.ShowToast(resourceProvider.getString(R.string.failchangenickname)))
            }
        }
    }

    fun updateProfileImage(imageUri: String) {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            _uiState.update { it.copy(isUpdateLoading = true) }
            try {
                val downloadUrl = uploadProfileImageUseCase(uid = myUid, photoUri = imageUri)
                updateUserProfileImageUseCase(userUid = myUid, photoUri = downloadUrl).fold(
                    onSuccess = { loadMyData() },
                    onFailure = { errorHandling(resourceProvider.getString(R.string.uploadimagefail)) }
                )
            } catch (e: FileNotFoundException) {
                errorHandling(resourceProvider.getString(R.string.filenotfound))
            } catch (e: Exception) {
                Log.d("imagfail", e.message.toString())
                errorHandling(resourceProvider.getString(R.string.uploadimagefail))
            }
        }
    }

    fun updateUserPosition(position: List<String>) {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            _uiState.update { it.copy(isUpdateLoading = true) }
          updateUserPositionUseCase(userUid = myUid, position = position).fold(
              onSuccess = { loadMyData() },
              onFailure = { errorHandling(resourceProvider.getString(R.string.defaulterror))
              }
          )
        }
    }

    fun updateUserHeight(height: Int?) {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            _uiState.update { it.copy(isUpdateLoading = true) }
            updateUserHeightUseCase(userUid = myUid, height = height).fold(
                onSuccess = { loadMyData() },
                onFailure = { errorHandling(resourceProvider.getString(R.string.defaulterror))}
            )
        }
    }

    fun updateUserWeight(weight: Int?) {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            _uiState.update { it.copy(isUpdateLoading = true) }
            updateUserWeightUseCase(userUid = myUid, weight = weight).fold(
                onSuccess = { loadMyData() },
                onFailure = { errorHandling(resourceProvider.getString(R.string.defaulterror))}
            )
        }
    }

    private suspend fun getCurrentUserId(): String? {
        return auth.currentUser?.uid ?: run {
            _uiEvent.emit(MyPageUiEvent.ShowToast(resourceProvider.getString(R.string.loselogin)))
            _uiEvent.emit(MyPageUiEvent.NavigateToLogin)
            null
        }
    }

    private  suspend fun errorHandling(errorMessage: String) {
        _uiEvent.emit(MyPageUiEvent.ShowToast(errorMessage))
        _uiState.update { it.copy(isUpdateLoading = false) }
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("MyPageViewModel", "onCleared")
    }
}


sealed class MyPageUiEvent {
    data class ShowToast(val message: String) : MyPageUiEvent()
    data object NavigateToLogin : MyPageUiEvent()
    data class ShowSnackBar(val message: String) : MyPageUiEvent()
    data object CompleteChangeNickname: MyPageUiEvent()
}

data class MyPageViewState(
    val dataState: DataState<User> = DataState.Loading,
    val isUpdateLoading: Boolean = false
)