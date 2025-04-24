package com.dkproject.presentation.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.ui.screen.Guest.GuestScreen
import com.dkproject.presentation.ui.screen.Guest.GuestViewModel
import com.dkproject.presentation.ui.screen.GuestDetail.GuestDetailScreen
import com.dkproject.presentation.ui.screen.GuestDetail.GuestDetailViewModel
import com.dkproject.presentation.ui.screen.GuestManage.GuestManageScreen
import com.dkproject.presentation.ui.screen.GuestManage.GuestManageViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun NavGraphBuilder.guestNavGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    composable(BottomNavItem.Guest.route) { backStackEntry ->
        val deleted = backStackEntry.savedStateHandle.getLiveData<Boolean>("post_deleted")

        val viewModel: GuestViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        LaunchedEffect(deleted) {
            Log.d("dong", "dong")
            if(deleted.value == true) {
                viewModel.refreshGuestList()
            }
        }
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
                navController.navigate(route = Screen.GuestDetail(encodedJson)) {
                    launchSingleTop = true
                }
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
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        GuestDetailScreen(
            uiState = uiState,
            uiEvent = viewModel.uiEvent,
            snackbarHostState = snackbarHostState,
            navPopBackStack = {
                navController.popBackStack()
            },
            retryAction = { viewModel.getPostInfo(guestPost) },
            applyButton = viewModel::applyButton,
            onRefresh = { viewModel.getPostInfo(guestPost) },
            navigateToMange = { navController.navigate(route = Screen.GuestManage(postId = guestPost.id ?: "")) },
            onEdit = {
                navController.navigate(route = Screen.Post(post = postDetail.post))
            },
            secessionAction = {
                viewModel.cancelGuest()
            },
            onDelete = {
                viewModel.deletePost()
            },
            onDeleteBack = {
                navController.previousBackStackEntry?.savedStateHandle?.set("post_deleted", true)
                navController.popBackStack()
            },
            onChatClick = { otherUserUid, otherUserNickname, otherProfileImage ->
                viewModel.onChatClick(otherUserUid = otherUserUid, otherUserNickname = otherUserNickname, otherProfileUrl = otherProfileImage)
            },
            navigateToChat = { chat ->
                navController.navigate(route = chat) {
                    popUpTo(Screen.GuestDetail(postDetail.post)) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }

    composable<Screen.GuestManage> { backStackEntry ->
        val guestManage: Screen.GuestManage = backStackEntry.toRoute()
        val postId = guestManage.postId

        val viewModel: GuestManageViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        LaunchedEffect(postId) {
            Log.d("GuestManageViewModel", "LaunchedEffect postId: $postId")
            viewModel.getManageList(postId)
        }
        GuestManageScreen(uiState = uiState,
            uiEvent = viewModel.uiEvent,
            snackbarHostState = snackbarHostState,
            onBackClick = { navController.popBackStack() },
            onAcceptClick =  { viewModel.acceptGuest(postId = postId, userId = it) },
            onRejectClick =  { viewModel.rejectGuest(postId = postId, userId = it) },
            onRefresh = { viewModel.refreshManageList(postId = postId) }
        )
    }
}