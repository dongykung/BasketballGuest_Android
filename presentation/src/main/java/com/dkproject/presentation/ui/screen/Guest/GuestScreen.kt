package com.dkproject.presentation.ui.screen.Guest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dkproject.domain.model.Guest.Coordinate
import com.dkproject.presentation.R
import com.dkproject.presentation.extension.withTime
import com.dkproject.presentation.model.GuestFilterUiModel
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.ui.component.GuestFilterTopBar
import com.dkproject.presentation.ui.component.sheet.PositionFilter
import com.dkproject.presentation.ui.component.util.EmptyGuestScreen
import com.dkproject.presentation.ui.component.util.ErrorScreen
import com.dkproject.presentation.ui.component.util.FooterErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen
import com.dkproject.presentation.ui.component.wheelpicker.DateWheelPicker
import com.dkproject.presentation.util.fetchLocation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun GuestScreen(
    uiState: GuestListUiState,
    updateGuestFilter: (GuestFilterUiModel) -> Unit,
    onRefresh: () -> Unit,
    onFilterReset: (GuestFilterUiModel) -> Unit,
    onNavigateToDetail: (GuestPostUiModel) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isDateFilter by remember { mutableStateOf(false) }
    var isPositionFilter by remember { mutableStateOf(false) }
    val postLists = uiState.guestList.collectAsLazyPagingItems()
    val dateBottomSheetState = rememberModalBottomSheetState(
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    val positionSheetState = rememberModalBottomSheetState(
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(Unit) {
            if (!notificationPermissionState.status.isGranted) {
                notificationPermissionState.launchPermissionRequest()
            }
        }
    }

    Column(modifier = modifier) {

        GuestFilterTopBar(
            guestFilter = uiState.guestFilter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .padding(start = 8.dp),
            onNearByClick = {
                handleLocationPermission(
                    context = context,
                    locationPermissionState = locationPermissionState,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    onPermissionGranted = {
                        coroutineScope.launch {
                            try {
                                val location = fetchLocation(context)
                                val coordinate = Coordinate(latitude = location.latitude, longitude = location.longitude)
                                val currentNearBy = uiState.guestFilter.isNearBy
                                val newFilter = uiState.guestFilter.copy(
                                    isNearBy = !currentNearBy,
                                    myLocation = coordinate
                                )
                                updateGuestFilter(newFilter)
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar(e.message ?: "오류 발생")
                            }
                        }
                    }
                )
            },
            onDateClick = { isDateFilter = true },
            onPositionClick = { isPositionFilter = true },
            onReset = {onFilterReset(GuestFilterUiModel())}
        )
        GuestPostsContent(
            postLists = postLists,
            modifier = Modifier.fillMaxSize(),
            onRetry = { postLists.retry() },
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            onNavigateToDetail = onNavigateToDetail
        )
    }
    if (isDateFilter) {
        ModalBottomSheet(sheetState = dateBottomSheetState, onDismissRequest = {}) {
            DateWheelPicker(date = uiState.guestFilter.selectedDate ?: Date(),
                onConfirmClick = { date ->
                    val newFilter =
                        uiState.guestFilter.copy(selectedDate = date.withTime(0, 0, 0, 0))
                    updateGuestFilter(newFilter)
                    isDateFilter = false
                }, onCancelClick = { isDateFilter = false })
        }
    }
    if (isPositionFilter) {
        ModalBottomSheet(sheetState = positionSheetState, onDismissRequest = {}) {
            PositionFilter(
                selectedPosition = uiState.guestFilter.selectedPosition,
                onCancelClick = { isPositionFilter = false },
                onConfirmClick = { positions ->
                    val newFilter = uiState.guestFilter.copy(selectedPosition = positions)
                    updateGuestFilter(newFilter)
                    isPositionFilter = false
                }
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun handleLocationPermission(
    context: Context,
    locationPermissionState: PermissionState,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onPermissionGranted: () -> Unit
) {
    when (locationPermissionState.status) {
        is PermissionStatus.Denied -> {
            if (locationPermissionState.status.shouldShowRationale) {
                // 권한 설명 가능 상태: 재요청
                locationPermissionState.launchPermissionRequest()
            } else {
                // "다시 묻지 않음" 상태: 권한 요청 후 스낵바 표시하여 설정으로 유도
                locationPermissionState.launchPermissionRequest()
                coroutineScope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.addresspermission),
                        actionLabel = context.getString(R.string.setting),
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        context.startActivity(intent)
                    }
                }
            }
        }
        PermissionStatus.Granted -> {
            onPermissionGranted()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestPostsContent(
    postLists: LazyPagingItems<GuestPostUiModel>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onNavigateToDetail: (GuestPostUiModel) -> Unit
) {
    when (val refreshState = postLists.loadState.refresh) {
        is LoadState.Error -> {
            ErrorScreen(
                retryAction = onRetry,
                errorMessage = refreshState.error.message ?: "오류 발생",
                modifier = Modifier.fillMaxSize()
            )
        }

        is LoadState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }

        is LoadState.NotLoading -> {
            if (postLists.itemCount == 0) {
                EmptyGuestScreen(modifier = Modifier.fillMaxSize())
            } else {
                PullToRefreshBox(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLow),
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh
                ) {
                    LazyColumn(modifier = modifier) {
                        items(postLists.itemCount, key = { it }) { index ->
                            postLists[index]?.let { guestPost ->
                                GuestListItem(
                                    guestPostUiModel = guestPost,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                        .clickable { onNavigateToDetail(guestPost) }
                                )
                            }
                        }
                        item {
                            when (postLists.loadState.append) {
                                is LoadState.Error -> {
                                    FooterErrorScreen(
                                        modifier = Modifier.fillMaxWidth(),
                                        retryAction = onRetry
                                    )
                                }

                                is LoadState.Loading -> {
                                    LoadingScreen(modifier = Modifier.fillMaxWidth())
                                }

                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}



