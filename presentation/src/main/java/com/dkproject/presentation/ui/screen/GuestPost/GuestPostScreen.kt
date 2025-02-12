package com.dkproject.presentation.ui.screen.GuestPost

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    viewModel: GuestPostViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    onUpload: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is PostUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(message = event.message)
                PostUiEvent.UploadComplete -> onUpload()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val progressValue by animateFloatAsState(targetValue = uiState.currentStep.progress, label = "")
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(uiState.currentStep.step),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.currentStep == GuestPostStep.Description) onBackClick()
                        else {
                            when (uiState.currentStep) {
                                GuestPostStep.Address -> viewModel.updateCurrentStep(GuestPostStep.Position)
                                GuestPostStep.GuestDate -> viewModel.updateCurrentStep(GuestPostStep.Description)
                                GuestPostStep.Position -> viewModel.updateCurrentStep(GuestPostStep.GuestDate)
                                else -> {}
                            }
                        }
                    }) {
                        Icon(
                            imageVector = if (uiState.currentStep == GuestPostStep.Description) Icons.Default.Close
                            else Icons.AutoMirrored.Default.ArrowBack, contentDescription = null
                        )
                    }
                }
            )
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { progressValue },
                color = MaterialTheme.colorScheme.inverseSurface,
                trackColor = MaterialTheme.colorScheme.onPrimary,
            )

            AnimatedContent(targetState = uiState.currentStep, label = "") { step ->
                when (step) {
                    GuestPostStep.Description -> DescriptionScreen(
                        title = uiState.guestPost.title,
                        description = uiState.guestPost.description,
                        titleErrorMessage = uiState.titleErrorMessage,
                        descriptionErrorMessage = uiState.descriptionErrorMessage,
                        titleChange = viewModel::updateTitle,
                        descriptionChange = viewModel::updateDescription,
                        onConfirmClick = { viewModel.updateCurrentStep(GuestPostStep.GuestDate) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )

                    GuestPostStep.Address -> {
                        GuestAddressScreen(
                            detailAddress = uiState.guestPost.placeName,
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            loading = uiState.isLoading,
                            onAddressChange = viewModel::updateAddress,
                            onConfirmClick = viewModel::uploadPost
                        )
                    }

                    GuestPostStep.GuestDate -> DateScreen(
                        date = uiState.guestPost.date,
                        startDate = uiState.guestPost.startDate,
                        endDate = uiState.guestPost.endDate,
                        onDateChange = { viewModel.updateDate(it) },
                        onStartDateChange = { viewModel.updateStartTime(it) },
                        onEndDateChange = { viewModel.updateEndTime(it) },
                        onConfirmClick = { viewModel.updateCurrentStep(GuestPostStep.Position) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )

                    GuestPostStep.Position -> GuestPositionScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        positions = uiState.guestPost.positions,
                        onPositionSelected = { viewModel.updatePosition(it) },
                        onConfirmClick = { viewModel.updateCurrentStep(GuestPostStep.Address) },
                        memberCount = uiState.guestPost.memberCount,
                        memberCountChange = { viewModel.updateMemberCount(it) }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GuestPostScreenPreview() {
    AppTheme {
        GuestPostScreen(snackbarHostState = remember { SnackbarHostState() })
    }
}