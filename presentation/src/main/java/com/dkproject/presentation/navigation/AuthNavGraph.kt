package com.dkproject.presentation.navigation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dkproject.presentation.ui.screen.login.LoginScreen
import com.dkproject.presentation.ui.screen.signUp.SignUpScreen
import com.dkproject.presentation.ui.screen.signUp.SignUpStep
import com.dkproject.presentation.ui.screen.signUp.SignUpViewModel
import com.dkproject.presentation.ui.screen.splash.SplashScreen

@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    composable<Screen.Splash> {
        SplashScreen(
            moveToLogin = {
                navController.navigate(Screen.Login) {
                    popUpTo(Screen.Splash) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            moveToHome = {
                navController.navigate(BottomNavItem.Guest.route) {
                    popUpTo(Screen.Splash) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
    composable<Screen.Login> {
        LoginScreen(snackbarHostState = snackbarHostState,
            moveToHome = {
                navController.navigate(BottomNavItem.Guest.route) {
                    popUpTo(Screen.Login) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            moveToSignUp = {
                navController.navigate(Screen.SignUp)
            })
    }
    composable<Screen.SignUp> {
        val viewModel: SignUpViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        val uiEvent = viewModel.uiEvent
        SignUpScreen(
            uiState = uiState,
            uiEvent = uiEvent,
            snackbarHostState = snackbarHostState,
            onBackClick = {
                if (uiState.currentStep == SignUpStep.Nickname) {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.SignUp) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                } else {
                    viewModel.onBackClick()
                }
            },
            onNextStep = { viewModel.nextStep() },
            onNicknameChange = { viewModel.updateNickName(it) },
            positionTap = { viewModel.positionToggle(it) },
            heightValueChange = { viewModel.updateUserHeight(it) },
            weightValueChange = { viewModel.updateUserWeight(it) },
            bodySkip = { viewModel.skipUserInfo() },
            moveToHome = {
                navController.navigate(BottomNavItem.Guest.route) {
                    popUpTo(Screen.SignUp) {
                        inclusive = true
                    }
                }
            }
        )
    }
}