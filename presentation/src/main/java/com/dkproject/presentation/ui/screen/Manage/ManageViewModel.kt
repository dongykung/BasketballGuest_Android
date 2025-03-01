package com.dkproject.presentation.ui.screen.Manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.usecase.MyData.GetMyPostListUseCase
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.model.toUiModel
import com.dkproject.presentation.ui.screen.GuestDetail.DetailUiEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getMyPostListUseCase: GetMyPostListUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ManageUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ManageUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        refreshMyPostList()
    }

    fun refreshMyPostList() {
        viewModelScope.launch {
            _uiState.update { it.copy(myPosts = buildMyPostListFlow()) }
        }
    }

    private suspend fun buildMyPostListFlow(): Flow<PagingData<GuestPostUiModel>> {
        val myUid = getCurrentUserId() ?: return emptyFlow()
        return getMyPostListUseCase(myUid = myUid)
            .flowOn(context = Dispatchers.IO)
            .map { it.map { domain -> domain.toUiModel() } }
            .cachedIn(viewModelScope)
    }

    private suspend fun getCurrentUserId(): String? {
        return auth.currentUser?.uid ?: run {
            _uiEvent.emit(ManageUiEvent.LoseLoginInfo)
            null
        }
    }
}


data class ManageUiState(
    val myPosts: Flow<PagingData<GuestPostUiModel>> = emptyFlow()
)

sealed class ManageUiEvent {
    data object LoseLoginInfo: ManageUiEvent()
}