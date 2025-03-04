package com.dkproject.presentation.navigation

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dkproject.presentation.ui.screen.Manage.ManageScreen
import com.dkproject.presentation.ui.screen.Manage.ManageViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun NavGraphBuilder.manageGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    composable(BottomNavItem.Manage.route) {
        val viewModel: ManageViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        ManageScreen(
            uiState = uiState,
            onNavigateToDetail = { post ->
                val json = Json.encodeToString(post)  // JSON 문자열 생성
                val encodedJson = Uri.encode(json)
                navController.navigate(route = Screen.GuestDetail(encodedJson)) {
                    launchSingleTop = true
                }
            },
            onRefreshMyPost = { viewModel.refreshMyPostList() },
            onRefreshMyParticipant = { viewModel.refreshMyParticipantList() },
            modifier = modifier
        )
    }
}