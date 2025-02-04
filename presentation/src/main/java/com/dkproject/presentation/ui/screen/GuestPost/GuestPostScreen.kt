package com.dkproject.presentation.ui.screen.GuestPost

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestPostScreen(
    viewModel: GuestPostViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.guestpost, uiState.currentStep.step))
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = if (uiState.currentStep == GuestPostStep.Description) Icons.Default.Close
                        else Icons.AutoMirrored.Default.ArrowBack, contentDescription = null
                    )
                }
            }
        )
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),
            progress = { uiState.currentStep.progress }
        )

        AnimatedContent(targetState = uiState.currentStep, label = "") { step ->
            when(step) {
                GuestPostStep.Description -> DescriptionScreen(
                    title = uiState.guestPost.title,
                    description = uiState.guestPost.description,
                    titleErrorMessage = uiState.titleErrorMessage,
                    descriptionErrorMessage = uiState.descriptionErrorMessage,
                    titleChange = viewModel::updateTitle,
                    descriptionChange = viewModel::updateDescription,
                    onConfirmClick = {},
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
                GuestPostStep.Address -> TODO()
                GuestPostStep.GuestDate -> TODO()
                GuestPostStep.Position -> TODO()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GuestPostScreenPreview() {
    AppTheme {
        GuestPostScreen()
    }
}