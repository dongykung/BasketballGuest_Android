package com.dkproject.presentation.navigation

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.ui.screen.Guest.GuestScreen
import com.dkproject.presentation.ui.screen.Guest.GuestViewModel
import com.dkproject.presentation.ui.screen.GuestDetail.GuestDetailScreen
import com.dkproject.presentation.ui.screen.GuestDetail.GuestDetailViewModel
import com.dkproject.presentation.ui.screen.myPage.MyPageScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.homeNavGraph(
    navController: NavController, snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

    composable(BottomNavItem.Guest.route) {
        val viewModel: GuestViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        GuestScreen(
            uiState = uiState,
            updateGuestFilter = viewModel::updateGuestFilter,
            onRefresh = viewModel::refreshGuestList,
            snackbarHostState = snackbarHostState,
            modifier = modifier,
            onFilterReset = viewModel::updateGuestFilter,
            onNavigateToDetail = { post ->
                val json = Json.encodeToString(post)  // JSON 문자열 생성
                val encodedJson = Uri.encode(json)
                navController.navigate(route = Screen.GuestDetail(encodedJson))
            }
        )
    }

    composable<Screen.GuestDetail> { backStackEntry ->
        val postDetail: Screen.GuestDetail = backStackEntry.toRoute()
        val decodedJson = Uri.decode(postDetail.post)
        val guestPost: GuestPostUiModel = Json.decodeFromString(decodedJson)

        val viewModel: GuestDetailViewModel = hiltViewModel()
        LaunchedEffect(guestPost) {
            viewModel.getPostInfo(guestPost)
        }
        val uiState by viewModel.uiState.collectAsState()
        GuestDetailScreen(
            uiState = uiState,
            uiEvent = viewModel.uiEvent,
            snackbarHostState = snackbarHostState,
            navPopBackStack = {
                navController.popBackStack()
            },
            retryAction = { viewModel.getPostInfo(guestPost) },
            applyButton = viewModel::applyButton,
            modifier = Modifier.fillMaxSize()
        )
    }

    composable(BottomNavItem.Chat.route) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "chat", modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }
    }
    composable(BottomNavItem.Profile.route) {
        MyPageScreen(moveToLogin = {
            navController.navigate(Screen.Login) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        })
    }
}