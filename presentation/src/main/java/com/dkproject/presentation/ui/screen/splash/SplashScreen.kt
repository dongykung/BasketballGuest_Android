package com.dkproject.presentation.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>(),
    moveToLogin: () -> Unit,
    moveToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState) {
        when(uiState) {
            AuthState.Authenticated -> {
                moveToHome()
            }
            AuthState.Unauthenticated -> {
                moveToLogin()
            }
            else -> {}
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.homebackground),
            contentDescription = stringResource(R.string.splash),
            contentScale = ContentScale.FillBounds
        )
        CircularProgressIndicator(
            modifier = Modifier.size(45.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SplashPreview() {
    AppTheme {
        SplashScreen(moveToLogin = {},moveToHome = {})
    }
}