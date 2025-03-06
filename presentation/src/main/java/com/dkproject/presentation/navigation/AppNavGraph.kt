package com.dkproject.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dkproject.domain.model.Chat.ChatRoom

@Composable
fun AppNavGraph(navController: NavHostController,
                modifier: Modifier = Modifier,
                snackbarHostState: SnackbarHostState,
                chatRoomList: List<ChatRoom> = emptyList(),
) {
    NavHost(navController = navController, startDestination = Screen.Splash) {
        authNavGraph(navController = navController, snackbarHostState = snackbarHostState)
        guestNavGraph(navController = navController, snackbarHostState = snackbarHostState, modifier = modifier)
        createGuestGraph(navController = navController, snackbarHostState = snackbarHostState)
        profileNavGraph(navController = navController, snackbarHostState = snackbarHostState, modifier = modifier)
        manageGraph(navController = navController, snackbarHostState = snackbarHostState, modifier = modifier)
        chatNavGraph(navController = navController, snackbarHostState = snackbarHostState, modifier = modifier, chatRoomList = chatRoomList)
    }
}

