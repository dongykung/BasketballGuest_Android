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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
import com.dkproject.presentation.ui.screen.myPage.MyPageScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

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