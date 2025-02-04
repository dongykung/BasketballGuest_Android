package com.dkproject.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dkproject.presentation.ui.screen.GuestPost.GuestPostScreen
import com.dkproject.presentation.ui.screen.GuestPost.GuestPostViewModel

fun NavGraphBuilder.createGuestGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    composable<Screen.Post> {
        GuestPostScreen()
    }
}