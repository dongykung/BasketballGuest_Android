package com.dkproject.presentation.ui.screen.searchAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dkproject.domain.model.Poi
import com.dkproject.domain.usecase.searchPlace.SearchPlaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchAddressViewModel @Inject constructor(
    private val searchPlaceUseCase: SearchPlaceUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()

    private val _searchTrigger = MutableStateFlow(0)


    val searchFlow: Flow<PagingData<Poi>> = combine(_searchQuery, _searchTrigger) { query, triger -> Pair(query, triger) }
            .debounce(500L)
            .distinctUntilChanged()
            .map {it.first}
            .filter { query ->
                query.isNotBlank()
            }
            .flatMapLatest { query ->
                _hasSearched.update { true }
                searchPlaceUseCase(query).cachedIn(viewModelScope)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), PagingData.empty())

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.update { newQuery }
    }

    fun performSearch() {
        _searchTrigger.value += 1
    }

}