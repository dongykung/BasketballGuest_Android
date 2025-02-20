package com.dkproject.presentation.ui.screen.GuestDetail

import android.content.Context
import android.util.Log
import androidx.compose.foundation.interaction.DragInteraction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.DataState
import com.dkproject.domain.model.User
import com.dkproject.domain.model.UserStatus
import com.dkproject.domain.usecase.Guest.ApplyGuestUseCase
import com.dkproject.domain.usecase.Guest.GetPostUserStatusUseCase
import com.dkproject.domain.usecase.auth.GetUserDataUseCase
import com.dkproject.presentation.R
import com.dkproject.presentation.di.ResourceProvider
import com.dkproject.presentation.model.GuestPostUiModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class GuestDetailViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getPostUserStatusUseCase: GetPostUserStatusUseCase,
    private val applyGuestUseCase: ApplyGuestUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<DetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        Log.d("GuestDetailViewModel", "init")
    }

    fun getPostInfo(postDetail: GuestPostUiModel) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            errorHandling(throwable)
        }
        _uiState.update { it.copy(dataState = DataState.Loading) }

        viewModelScope.launch(handler) {
            val myUid = getCurrentUserId() ?: return@launch
            val user = async(context = Dispatchers.IO) {
                getUserDataUseCase(userUid = postDetail.writerUid)
            }
            val status = async(context = Dispatchers.IO) {
                if(postDetail.writerUid == myUid) UserStatus.OWNER
                else getPostUserStatusUseCase(postUid = postDetail.id ?: "", userUid = myUid)
            }
            val result = DataState.Success(
                PostDetailDataState(
                    postDetail = postDetail,
                    writerInfo = user.await(),
                    myStatus = status.await()
                )
            )
            _uiState.update { it.copy(dataState = result) }
        }
    }

    private fun errorHandling(e: Throwable) {
        viewModelScope.launch {
             when (e) {
                is NoSuchElementException -> {
                    _uiEvent.emit(DetailUiEvent.NavPopBackStack(resourceProvider.getString(R.string.nosuch)))
                }
                is IOException, is FirebaseFirestoreException -> {
                    _uiState.update { it.copy(dataState = DataState.Error(resourceProvider.getString(R.string.networkerror))) }
                }
                else -> {
                    _uiState.update { it.copy(dataState = DataState.Error(resourceProvider.getString(R.string.defaulterror))) }
                }
            }
        }
    }

    fun applyButton(userStatus: UserStatus) {
        when(userStatus) {
            UserStatus.OWNER -> {}
            UserStatus.GUEST -> {}
            UserStatus.APPLY -> {}
            UserStatus.NONE -> applyGuest()
        }
    }

    private fun applyGuest() {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            val postDetail = getPostDetailData() ?: return@launch

            setStatusLoading(true)
            try {
                withContext(context = Dispatchers.IO) {
                    applyGuestUseCase(postUid = postDetail.postDetail.id ?: "", userUid = myUid)
                }
                _uiEvent.emit(DetailUiEvent.ShowSnackbar(resourceProvider.getString(R.string.completeapply)))
                _uiState.update { it.copy(dataState = DataState.Success(postDetail.copy(myStatus = UserStatus.APPLY)), statusLoading = false) }
            } catch (e: Exception) {
                _uiEvent.emit(DetailUiEvent.ShowSnackbar(resourceProvider.getString(R.string.failapply)))
                setStatusLoading(false)
            }
        }
    }

    private suspend fun getCurrentUserId(): String? {
        return auth.currentUser?.uid ?: run {
            _uiEvent.emit(DetailUiEvent.LoseLoginInfo)
            null
        }
    }

    private suspend fun getPostDetailData(): PostDetailDataState? {
        return when (val currentState = uiState.value.dataState) {
            is DataState.Success -> currentState.data
            else -> {
                _uiEvent.emit(DetailUiEvent.ShowSnackbar("포스트 정보가 유효하지 않습니다."))
                null
            }
        }
    }

    private fun setStatusLoading(isLoading: Boolean) {
        _uiState.update { it.copy(statusLoading = isLoading) }
    }
}


data class PostDetailUiState(
    val dataState: DataState<PostDetailDataState> = DataState.Loading,
    val statusLoading: Boolean = false,
)

data class PostDetailDataState(
    val postDetail: GuestPostUiModel,
    val writerInfo: User,
    val myStatus: UserStatus
)

sealed class DetailUiEvent {
    data class ShowSnackbar(val message: String) : DetailUiEvent()
    data object LoseLoginInfo : DetailUiEvent()
    data class NavPopBackStack(val message: String) : DetailUiEvent()
}