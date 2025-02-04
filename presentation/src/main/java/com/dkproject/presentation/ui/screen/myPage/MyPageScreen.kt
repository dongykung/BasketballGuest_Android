package com.dkproject.presentation.ui.screen.myPage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.TextDialog

@Composable
fun MyPaceScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    moveToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MyPageUiEvent.NavigateToLogin -> {
                    viewModel.onEvent(MyPageUiEvent.HideLogoutConfirmation)
                    moveToLogin()
                }
                else -> {}
            }
        }
    }

    Column() {
        if(uiState.isLogoutDialogVisible) {
            TextDialog(
                title = stringResource(R.string.logout),
                message = stringResource(R.string.logoutmessage),
                onDismiss = {viewModel.onEvent(MyPageUiEvent.HideLogoutConfirmation)},
                onConfirm = {viewModel.onEvent(MyPageUiEvent.LogOut)}
            )
        }
        TextButton(onClick = {viewModel.onEvent(MyPageUiEvent.ShowLogoutConfirmation)}) {
            Text("logout")
        }
    }

}