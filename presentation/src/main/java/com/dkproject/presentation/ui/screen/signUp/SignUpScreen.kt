package com.dkproject.presentation.ui.screen.signUp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dkproject.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    uiState: SignUpViewState,
    uiEvent: SharedFlow<SignUpUiEvent>,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onNextStep: () -> Unit,
    onNicknameChange: (String) -> Unit,
    heightValueChange: (Int) -> Unit,
    weightValueChange: (Int) -> Unit,
    bodySkip: () -> Unit,
    positionTap: (Position) -> Unit,
    moveToHome: () -> Unit
) {
    val progressValue by animateFloatAsState(targetValue = uiState.currentStep.progress, label = "")

    LaunchedEffect(uiEvent) {
        uiEvent.collect { event ->
            when (event) {
                is SignUpUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is SignUpUiEvent.MoveToHome -> {
                    moveToHome()
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(uiState.currentStep.title)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.signupback)
                    )
                }
            })
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = { progressValue },
        )

        AnimatedContent(targetState = uiState.currentStep, label = "") { step ->
            when (step) {
                SignUpStep.Nickname -> {
                    NicknameScreen(
                        nickname = uiState.user.nickName,
                        errorMessage = uiState.errorMessage,
                        loading = uiState.isLoading,
                        onNicknameChange = onNicknameChange,
                        onConfirmClick = onNextStep,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }

                SignUpStep.UserInfo -> {
                    UserBodyInfoScreen(
                        onConfirmClick = onNextStep,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        height = uiState.user.height,
                        weight = uiState.user.weight,
                        loading = uiState.isLoading,
                        heightValueChange = heightValueChange,
                        weightValueChange = weightValueChange,
                        onSkip = bodySkip
                    )
                }

                SignUpStep.Position -> {
                    PositionScreen(
                        positions = uiState.user.position,
                        positionTap = positionTap,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        onConfirmClick = onNextStep
                    )
                }
            }
        }
    }
}

