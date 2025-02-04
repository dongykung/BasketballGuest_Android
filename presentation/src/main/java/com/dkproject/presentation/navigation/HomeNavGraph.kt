package com.dkproject.presentation.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dkproject.presentation.ui.screen.myPage.MyPageScreen

@SuppressLint("ComposableDestinationInComposeScope")
fun NavGraphBuilder.HomeNavGraph(navController: NavController,modifier: Modifier = Modifier) {


    composable(BottomNavItem.Guest.route) {
        Column(modifier = modifier.fillMaxSize()) {
            Text(text = "tt")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "test",
                modifier = Modifier
            )
        }
    }
    composable(BottomNavItem.Chat.route) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "chat", modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray))
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