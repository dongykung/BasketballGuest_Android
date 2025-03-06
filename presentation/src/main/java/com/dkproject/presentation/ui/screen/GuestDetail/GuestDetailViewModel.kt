package com.dkproject.presentation.ui.screen.GuestDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.DataState
import com.dkproject.domain.model.User.User
import com.dkproject.domain.model.User.UserStatus
import com.dkproject.domain.usecase.Guest.ApplyGuestUseCase
import com.dkproject.domain.usecase.Guest.CancelGuestUseCase
import com.dkproject.domain.usecase.Guest.DeleteGuestPostUseCase
import com.dkproject.domain.usecase.Guest.GetPostDataUseCase
import com.dkproject.domain.usecase.Guest.GetPostUserStatusUseCase
import com.dkproject.domain.usecase.auth.DeleteMyParticipantUseCase
import com.dkproject.domain.usecase.auth.GetUserDataUseCase
import com.dkproject.domain.usecase.auth.SetApplyGuestUseCase
import com.dkproject.presentation.R
import com.dkproject.presentation.di.ResourceProvider
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.model.toUiModel
import com.dkproject.presentation.navigation.Screen
import com.dkproject.presentation.util.generateChatRoomId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
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
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class GuestDetailViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getPostDataUseCase: GetPostDataUseCase,                 // 포스팅 데이터를 가져오는 UseCase
    private val getUserDataUseCase: GetUserDataUseCase,                 // 작성자 정보를 가져오는 UseCase
    private val getPostUserStatusUseCase: GetPostUserStatusUseCase,     // 포스팅에 대한 유저 상태를 가져오는 UseCase
    private val applyGuestUseCase: ApplyGuestUseCase,                   // 게스트 신청하기 UseCase
    private val setMyApplyGuestUseCase: SetApplyGuestUseCase,           // 내 신청 목록에 추가하는 UseCase
    private val cancelGuestUseCase: CancelGuestUseCase,                 // 신청취소 UseCase
    private val deleteMyParticipantUseCase: DeleteMyParticipantUseCase, // 내 신청 목록에서 삭제하는 UseCase
    private val deleteGuestPostUseCase: DeleteGuestPostUseCase,         //글 삭제 UseCase
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
            val postData = async(context = Dispatchers.IO) {
                getPostDataUseCase(postUid = postDetail.id ?: "").toUiModel()
            }
            val status = async(context = Dispatchers.IO) {
                if (postDetail.writerUid == myUid) UserStatus.OWNER
                else getPostUserStatusUseCase(postUid = postDetail.id ?: "", userUid = myUid)
            }
            val result = DataState.Success(
                PostDetailDataState(
                    postDetail = postData.await(),
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
                    _uiEvent.emit(DetailUiEvent.NoSuchData(resourceProvider.getString(R.string.nosuch)))
                }

                is IOException, is FirebaseFirestoreException -> {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                resourceProvider.getString(
                                    R.string.networkerror
                                )
                            )
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            dataState = DataState.Error(
                                resourceProvider.getString(
                                    R.string.defaulterror
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun applyButton(userStatus: UserStatus) {
        when (userStatus) {
            UserStatus.OWNER -> viewModelScope.launch { _uiEvent.emit(DetailUiEvent.ManageGuest) }
            UserStatus.GUEST -> {}
            UserStatus.DENIED -> {}
            UserStatus.APPLY -> cancelGuest()
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
                    setMyApplyGuestUseCase(myUid = myUid, postUid = postDetail.postDetail.id ?: "")
                }
                _uiEvent.emit(DetailUiEvent.ShowSnackbar(resourceProvider.getString(R.string.completeapply)))
                _uiState.update {
                    it.copy(
                        dataState = DataState.Success(postDetail.copy(myStatus = UserStatus.APPLY)),
                        statusLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiEvent.emit(DetailUiEvent.ShowSnackbar(resourceProvider.getString(R.string.failapply)))
                setStatusLoading(false)
            }
        }
    }

    fun cancelGuest() {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            val postDetail = getPostDetailData() ?: return@launch
            setStatusLoading(true)

            try {
                withContext(context = Dispatchers.IO) {
                    cancelGuestUseCase(postUid = postDetail.postDetail.id ?: "", userUid = myUid)
                    deleteMyParticipantUseCase(
                        myUid = myUid,
                        postUid = postDetail.postDetail.id ?: ""
                    )
                }
                _uiEvent.emit(DetailUiEvent.ShowSnackbar(resourceProvider.getString(R.string.completecancel)))
                _uiState.update {
                    it.copy(
                        dataState = DataState.Success(postDetail.copy(myStatus = UserStatus.NONE)),
                        statusLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiEvent.emit(DetailUiEvent.ShowSnackbar(resourceProvider.getString(R.string.failcancel)))
                setStatusLoading(false)
            }
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleteLoading = true) }
            val postData = getPostDetailData() ?: return@launch
            val result = deleteGuestPostUseCase(postData.postDetail.id ?: "")
            result.fold(onSuccess = {
                _uiEvent.emit(DetailUiEvent.PopBackStack)
            }, onFailure = {
                _uiState.update { it.copy(isDeleteLoading = false) }
                _uiEvent.emit(DetailUiEvent.ShowSnackbar(resourceProvider.getString(R.string.faildeletepost)))
            })
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

    fun onChatClick(otherUserUid: String, otherUserNickname: String, otherProfileUrl: String) {
        viewModelScope.launch {
            val myUid = getCurrentUserId() ?: return@launch
            val chatRoomId = generateChatRoomId(uid1 = myUid, uid2 = otherUserUid)
            _uiEvent.emit(
                DetailUiEvent.NavigateChat(
                    Screen.Chat(
                        chatRoomId = chatRoomId,
                        otherUserUid = otherUserUid,
                        otherUserName = otherUserNickname,
                        otherProfileUrl = otherProfileUrl
                    )
                )
            )
        }
    }

    private fun setStatusLoading(isLoading: Boolean) {
        _uiState.update { it.copy(statusLoading = isLoading) }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GuestDetailViewModel", "onCleared")
    }
}


data class PostDetailUiState(
    val dataState: DataState<PostDetailDataState> = DataState.Loading,
    val statusLoading: Boolean = false,
    val isDeleteLoading: Boolean = false,
)

data class PostDetailDataState(
    val postDetail: GuestPostUiModel,
    val writerInfo: User,
    val myStatus: UserStatus
)

sealed class DetailUiEvent {
    data class ShowSnackbar(val message: String) : DetailUiEvent()
    data object LoseLoginInfo : DetailUiEvent()
    data class NoSuchData(val message: String) : DetailUiEvent()
    data object PopBackStack : DetailUiEvent()
    data object DeleteCompletedPost : DetailUiEvent()
    data object ManageGuest : DetailUiEvent()
    data class NavigateChat(val chat: Screen.Chat) : DetailUiEvent()
}