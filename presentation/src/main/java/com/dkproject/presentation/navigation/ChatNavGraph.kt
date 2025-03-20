package com.dkproject.presentation.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.dkproject.domain.model.Chat.Chat
import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.presentation.ui.screen.Chat.ChatScreen
import com.dkproject.presentation.ui.screen.Chat.ChatUiEvent
import com.dkproject.presentation.ui.screen.Chat.ChatViewModel
import com.dkproject.presentation.ui.screen.ChatRoom.ChatRoomScreen
import com.google.api.ChangeType

fun NavGraphBuilder.chatNavGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    chatRoomList: List<ChatRoom> = emptyList(),
) {
    composable(BottomNavItem.Chat.route) {


        ChatRoomScreen(chatRoomList = chatRoomList, modifier = modifier,
            navigateToChat = { chat ->
                navController.navigate(route = chat) {
                    popUpTo(BottomNavItem.Chat.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
    }

    composable<Screen.Chat> { backStackEntry ->
        val chat: Screen.Chat = backStackEntry.toRoute()
        val viewModel: ChatViewModel = hiltViewModel()

        LaunchedEffect(chat) {
            Log.d("chat", "getChatList")
            viewModel.getChatList(chatRoomId = chat.chatRoomId)
        }
        LaunchedEffect(viewModel.uiEvent) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    ChatUiEvent.NavigateToLogin -> {}
                    is ChatUiEvent.ShowToast -> {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
        ChatScreen(
            viewModel = viewModel,
            otherUserUid = chat.otherUserUid,
            otherUserNickname = chat.otherUserName,
            otherUserProfile = chat.otherProfileUrl,
            onBackClick = { navController.popBackStack() },
            onSendClick = {
                viewModel.sendMessage(chat)
            },
            updateChatMessage = viewModel::updateChatMessage,
            fetchedLastMessages = { lastFetched ->
                viewModel.getLatestMessage(chatRoomId = chat.chatRoomId, lastFetched = lastFetched)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}