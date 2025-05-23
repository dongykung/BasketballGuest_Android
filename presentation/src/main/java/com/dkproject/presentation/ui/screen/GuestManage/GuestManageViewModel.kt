package com.dkproject.presentation.ui.screen.GuestManage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dkproject.domain.Error.DomainError
import com.dkproject.domain.model.GuestManage.GuestManage
import com.dkproject.domain.model.User.UserStatus
import com.dkproject.domain.usecase.Guest.AcceptGuestUseCase
import com.dkproject.domain.usecase.Guest.GetGuestManageListUseCase
import com.dkproject.domain.usecase.Guest.RejectGuestUseCase
import com.dkproject.presentation.R
import com.dkproject.presentation.di.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuestManageViewModel @Inject constructor(
    private val getGuestManageListUseCase: GetGuestManageListUseCase,
    private val acceptGuestUseCase: AcceptGuestUseCase,
    private val rejectGuestUseCase: RejectGuestUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _uiState: MutableStateFlow<GuestManageUiState> =
        MutableStateFlow(GuestManageUiState())
    val uiState: StateFlow<GuestManageUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<GuestManageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun getManageList(postId: String) {
        _uiState.update {
            it.copy(
                guestManageList = buildManageListFlow(postId = postId),
                changeStatusItems = emptyMap(),
                isLoading = false
            )
        }
    }

    private fun buildManageListFlow(postId: String): Flow<PagingData<GuestManage>> {
        return getGuestManageListUseCase(postUid = postId)
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
    }

    fun acceptGuest(postId: String, userId: String) {
        viewModelScope.launch {
            updateIsLoadingState(true)
            acceptGuestUseCase(postUid = postId, userUid = userId).fold(
                onSuccess = {
                    val changeStatus = uiState.value.changeStatusItems.toMutableMap()
                    changeStatus[userId] = UserStatus.GUEST
                    _uiState.update { it.copy(changeStatusItems = changeStatus, isLoading = false) }
                },
                onFailure = { errorHandling(it) }
            )
        }
    }


    fun rejectGuest(postId: String, userId: String) {
        viewModelScope.launch {
            updateIsLoadingState(true)
            rejectGuestUseCase(postUid = postId, userUid = userId).fold(
                onSuccess = {
                    val changeStatus = uiState.value.changeStatusItems.toMutableMap()
                    changeStatus[userId] = UserStatus.DENIED
                    _uiState.update { it.copy(changeStatusItems = changeStatus, isLoading = false) }
                },
                onFailure = { errorHandling(it) }
            )
        }
    }

    fun refreshManageList(postId: String) {
        _uiState.update { it.copy(isRefreshLoading = true) }
        _uiState.update {
            it.copy(
                guestManageList = buildManageListFlow(postId = postId),
                changeStatusItems = emptyMap(),
                isRefreshLoading = false,
                isLoading = false
            )
        }
    }

    private fun updateIsLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private suspend fun errorHandling(e: Throwable) {
        updateIsLoadingState(false)
        when (e) {
            is DomainError.DocumentNotFound -> _uiEvent.send(
                GuestManageUiEvent.ShowMessage(
                    resourceProvider.getString(R.string.managenotfound)
                )
            )
            else -> _uiEvent.send(GuestManageUiEvent.ShowMessage(resourceProvider.getString(R.string.failreject)))
        }
    }
}

data class GuestManageUiState(
    val guestManageList: Flow<PagingData<GuestManage>> = emptyFlow(),
    val changeStatusItems: Map<String, UserStatus> = emptyMap(),
    val isRefreshLoading: Boolean = false,
    val isLoading: Boolean = false
)

sealed class GuestManageUiEvent {
    data class ShowMessage(val message: String) : GuestManageUiEvent()
}