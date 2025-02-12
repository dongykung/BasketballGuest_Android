package com.dkproject.presentation.ui.screen.Guest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.dkproject.domain.model.GuestFilter
import com.dkproject.domain.usecase.Guest.GetGuestPostListUseCase
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GuestViewModel @Inject constructor(
    private val getGuestPostListUseCase: GetGuestPostListUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _uiState: MutableStateFlow<GuestListUiState> = MutableStateFlow(GuestListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getGuestList()
    }

    fun getGuestList() {
        val guestListFlow = buildGuestListFlow(uiState.value.guestFilter)
        _uiState.update { currentState ->
            currentState.copy(guestList = guestListFlow)
        }
    }

    private fun buildGuestListFlow(filter: GuestFilter): Flow<PagingData<GuestPostUiModel>> {
        return getGuestPostListUseCase(filter)
            .map { pagingData -> pagingData.map { it.toUiModel() } }
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }
}

data class GuestListUiState(
    val guestList: Flow<PagingData<GuestPostUiModel>> = emptyFlow(),
    val guestFilter: GuestFilter = GuestFilter(selectedDate = null),
)

sealed class GuestUiEvent {
    data class ShowSnackbar(val message: String) : GuestUiEvent()
}