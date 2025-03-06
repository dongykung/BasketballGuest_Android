package com.dkproject.presentation.ui.component

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.fadeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsBaseball
import androidx.compose.material.icons.outlined.SportsBaseball
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.presentation.R
import com.dkproject.presentation.navigation.BottomNavItem
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BottomNavigation(
    navController: NavController,
    chatRoomList: List<ChatRoom>
) {
    val auth = FirebaseAuth.getInstance()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        BottomNavItem.Guest,
        BottomNavItem.Chat,
        BottomNavItem.Manage,
        BottomNavItem.Profile
    )
    val unReadCount: Int = chatRoomList.sumOf { it.unReadCount[auth.currentUser?.uid] ?: 0 }
    AnimatedVisibility(
        visible = items.map { it.route }.contains(currentRoute),
        enter = fadeIn()
    ) {
        NavigationBar() {
            items.forEach { item ->
                val selected = item.route == currentRoute
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        if(item == BottomNavItem.Chat)
                        BadgedBox(
                            badge = {
                                if (unReadCount >0) {
                                    Badge() { Text(text = "$unReadCount") }
                                }
                            }
                        ) {
                            Icon(imageVector = getIcon(item.title), contentDescription = stringResource(item.description))
                        }
                         else Icon(imageVector = getIcon(item.title), contentDescription = stringResource(item.description))

                    },
                    label = {
                        Text(text = stringResource(item.description),
                            color = if(selected) MaterialTheme.colorScheme.scrim else Color.Unspecified,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondaryContainer,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

private fun getIcon(@StringRes title: Int): ImageVector {
    return when (title) {
        R.string.guest -> Icons.Filled.SportsBaseball
        R.string.chat -> Icons.Filled.ChatBubble
        R.string.mypage -> Icons.Filled.Person
        R.string.bottomitemmanage -> Icons.AutoMirrored.Filled.Assignment
        else -> Icons.Filled.SportsBaseball
    }
}