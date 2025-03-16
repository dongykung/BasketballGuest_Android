package com.dkproject.presentation.ui.screen.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dkproject.domain.usecase.auth.CheckFirstUserUseCase
import com.dkproject.domain.usecase.auth.SetFcmTokenUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val checkFirstUserUseCase: CheckFirstUserUseCase,
    private val setFcmTokenUseCase: SetFcmTokenUseCase

): ViewModel() {
    private val _uiState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    init {
        checkUserAuthentication()
    }

    private fun checkUserAuthentication() {
        val myUid = Firebase.auth.currentUser?.uid
        viewModelScope.launch {

            if (checkFirstUserUseCase(uid = myUid ?: "").getOrElse { false }) {
                try {
                    setFcmTokenUseCase(myUid ?: "")
                    _uiState.update { AuthState.Authenticated }
                } catch (e: Exception) {
                    _uiState.update { AuthState.Unauthenticated }
                }
            } else {
                _uiState.update { AuthState.Unauthenticated }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("SplashViewModel", "SplashViewModel cleared")
    }
}

sealed class AuthState {
    data object Unauthenticated : AuthState()
    data object Authenticated : AuthState()
    data object Loading : AuthState()
}