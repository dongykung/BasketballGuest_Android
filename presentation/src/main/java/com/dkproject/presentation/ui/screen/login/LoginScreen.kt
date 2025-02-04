package com.dkproject.presentation.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.button.GoogleConfig
import com.dkproject.presentation.ui.component.button.KakaoConfig
import com.dkproject.presentation.ui.component.button.SocialButton
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    moveToHome: () -> Unit,
    moveToSignUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is LoginUiEvent.MoveToHome -> moveToHome()
                is LoginUiEvent.MoveToSignUp -> moveToSignUp()
                is LoginUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.homebackground),
            contentDescription = stringResource(R.string.login),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.weight(7f))
            SocialButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                socialConfiguarble = GoogleConfig(),
                onClick = {
                    viewModel.signInWithGoogle()
                }
            )
            SocialButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                socialConfiguarble = KakaoConfig(),
                onClick = {viewModel.signWitnKakako()}
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LoginScreenPreview() {
    AppTheme {

    }
}