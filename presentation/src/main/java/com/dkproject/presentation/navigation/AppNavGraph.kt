package com.dkproject.presentation.navigation

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph(navController: NavHostController,
                modifier: Modifier = Modifier,
                snackbarHostState: SnackbarHostState) {
    NavHost(navController = navController, startDestination = Screen.Post) {
        authNavGraph(navController = navController, snackbarHostState = snackbarHostState)
        HomeNavGraph(navController = navController, modifier = modifier)
        createGuestGraph(navController = navController, snackbarHostState = snackbarHostState)
    }
}

