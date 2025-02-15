package com.dkproject.presentation.ui.screen.GuestDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.theme.AppTheme
import com.dkproject.presentation.util.GetUserStatusString
import com.dkproject.presentation.util.guestPostDummy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestDetailScreen(
    uiState: PostDetailUiState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val postDetail = uiState.postDetail
    val scrollState = rememberScrollState()
    Box(modifier = modifier) {
        Column(modifier = Modifier) {
            TopAppBar(title = {},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            null
                        )
                    }
                },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.MoreHoriz, null) } }
            )
            Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(horizontal = 16.dp)) {
                Text(text = postDetail.title,
                    style = MaterialTheme.typography.titleLarge)
            }
        }
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(vertical = 16.dp, horizontal = 16.dp),
            title = stringResource(GetUserStatusString(uiState.myStatus)),
            onClick = {},
            loading = uiState.isMyStatusLoading
        )
    }
}


@Composable
@Preview(showBackground = true)
private fun GuestDetailScreenPreview() {
    AppTheme {
        GuestDetailScreen(
            uiState = PostDetailUiState(guestPostDummy),
            snackbarHostState = SnackbarHostState(),
            modifier = Modifier
                .fillMaxSize()
                .padding()
        )

    }
}