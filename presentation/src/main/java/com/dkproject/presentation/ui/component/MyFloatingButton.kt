package com.dkproject.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dkproject.presentation.R
import com.dkproject.presentation.navigation.BottomNavItem
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun MyFloatingButton(
    navController: NavController,
    onClick:() -> Unit = {}
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AnimatedVisibility(currentRoute == BottomNavItem.Guest.route,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MyFloatingButtonPreview() {
    AppTheme {
        MyFloatingButton(navController = rememberNavController()) { }
    }
}