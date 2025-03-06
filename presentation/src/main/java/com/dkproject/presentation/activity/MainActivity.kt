package com.dkproject.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.dkproject.presentation.navigation.AppNavGraph
import com.dkproject.presentation.ui.component.BottomNavigation
import com.dkproject.presentation.ui.component.HomeTopAppBar
import com.dkproject.presentation.ui.component.button.MyFloatingButton
import com.dkproject.presentation.ui.screen.ChatRoom.ChatRoomUiEvent
import com.dkproject.presentation.ui.screen.ChatRoom.ChatRoomViewModel
import com.dkproject.presentation.ui.screen.searchAddress.SearchAddressScreen
import com.dkproject.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val chatRoomViewModel: ChatRoomViewModel = hiltViewModel()
                val chatRoomList by chatRoomViewModel.chatRoomList.collectAsStateWithLifecycle()
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        HomeTopAppBar(
                            navController = navController,
                            scrollBehavior = scrollBehavior
                        )
                    },
                    bottomBar = { BottomNavigation(navController = navController, chatRoomList = chatRoomList) },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    floatingActionButton = { MyFloatingButton(navController = navController) }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        snackbarHostState = snackbarHostState,
                        chatRoomList = chatRoomList,
                    )
                }
            }
        }
    }
}