package com.dkproject.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.outlined.SportsBaseball
import androidx.compose.ui.graphics.vector.ImageVector
import com.dkproject.presentation.R
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object SignUp: Screen
}

@Serializable
sealed class BottomNavItem(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val route: String
) {
    @Serializable
    data object Guest : BottomNavItem(R.string.guest, R.string.guest, "guest")

    @Serializable
    data object Chat : BottomNavItem(R.string.chat, R.string.chat, "chat")

    @Serializable
    data object Profile : BottomNavItem(R.string.mypage, R.string.mypage,"profile")
}


