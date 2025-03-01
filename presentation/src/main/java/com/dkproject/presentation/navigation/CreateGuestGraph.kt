package com.dkproject.presentation.navigation

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.ui.screen.GuestPost.GuestPostScreen
import com.dkproject.presentation.ui.screen.GuestPost.GuestPostViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun NavGraphBuilder.createGuestGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    composable<Screen.Post> { backStackEntry ->
        val viewModel: GuestPostViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        val post: Screen.Post = backStackEntry.toRoute()
        LaunchedEffect(post) {
            if(post.post != null) {
                val decodedJson = Uri.decode(post.post)
                val guestPost: GuestPostUiModel = Json.decodeFromString(decodedJson)
                viewModel.updateEditMode(guestPost = guestPost)
            }
        }

        GuestPostScreen(
            uiState = uiState,
            uiEvent = viewModel.uiEvent,
            snackbarHostState = snackbarHostState,
            updateCurrentStep = viewModel::updateCurrentStep,
            titleChange = viewModel::updateTitle,
            descriptionChange = viewModel::updateDescription,
            updateAddress = viewModel::updateAddress,
            uploadPost = viewModel::uploadPost,
            onDateChange = viewModel::updateDate,
            updateStartTime = viewModel::updateStartTime,
            updateEndTime = viewModel::updateEndTime,
            onPositionSelected = viewModel::updatePosition,
            memberCountChange = viewModel::updateMemberCount,
            onBackClick = {
                navController.popBackStack()
            },
            onUpload = {
                navController.navigate(BottomNavItem.Guest.route) {
                    popUpTo(Screen.Post(null)) {
                        inclusive = true
                    }
                }
            },
            onEdit = {
                val json = Json.encodeToString(uiState.guestPost)  // JSON 문자열 생성
                val encodedJson = Uri.encode(json)
                navController.navigate(route = Screen.GuestDetail(encodedJson)) {
                    popUpTo(Screen.GuestDetail(post.post ?: "")) {
                        inclusive = true
                    }
                }
            }
        )
    }
}