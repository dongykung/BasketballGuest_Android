package com.dkproject.presentation.ui.screen.GuestDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.model.DataState
import com.dkproject.domain.model.User
import com.dkproject.domain.model.UserStatus
import com.dkproject.domain.usecase.Guest.GetPostUserStatusUseCase
import com.dkproject.domain.usecase.auth.GetUserDataUseCase
import com.dkproject.presentation.model.GuestPostUiModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class GuestDetailViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getUserDataUser: GetUserDataUseCase,
    private val getPostUserStatusUseCase: GetPostUserStatusUseCase
    ): ViewModel() {

    private val _postDetailUiState = MutableStateFlow(PostDetailUiState())
    val postDetailUiState: StateFlow<PostDetailUiState> = _postDetailUiState.asStateFlow()

    fun setPostDetail(postDetail: GuestPostUiModel) {
       
    }

    fun getPostInfo() {
        viewModelScope.launch {
            val df = async {  }
        }
    }
}

data class PostDetailUiState(
    val dataState: DataState<PostDetailDataState> = DataState.Loading,
    val writerInfo: User = User(),
    val myStatus: UserStatus = UserStatus.NONE,
    val isLoading: Boolean = true,
    val isMyStatusLoading: Boolean = true,
)
data class PostDetailDataState(
        val postDetail: GuestPostUiModel,
        val writerInfo: User,
        val myStatus: UserStatus
)