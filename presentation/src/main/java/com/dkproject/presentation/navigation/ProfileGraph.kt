package com.dkproject.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dkproject.presentation.ui.screen.myPage.MyPageScreen

@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.profileNavGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    composable(BottomNavItem.Profile.route) {
        MyPageScreen(
            moveToLogin = {
                navController.navigate(Screen.Login) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            snackbarHostState = snackbarHostState,
            modifier = modifier
        )
    }
}