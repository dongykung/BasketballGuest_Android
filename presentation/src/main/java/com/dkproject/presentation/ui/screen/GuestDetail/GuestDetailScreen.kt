package com.dkproject.presentation.ui.screen.GuestDetail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dkproject.domain.model.DataState
import com.dkproject.domain.model.User
import com.dkproject.domain.model.UserStatus
import com.dkproject.presentation.R
import com.dkproject.presentation.extension.startTimeWithEndTime
import com.dkproject.presentation.extension.toFormattedFilterDate
import com.dkproject.presentation.extension.toFormattedHomeGuestListString
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.model.Position
import com.dkproject.presentation.ui.component.DefaultProfileImage
import com.dkproject.presentation.ui.component.PositionChip2
import com.dkproject.presentation.ui.component.TextDialog
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.component.util.ErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen
import com.dkproject.presentation.ui.theme.AppTheme
import com.dkproject.presentation.util.GetUserStatusString
import com.dkproject.presentation.util.guestPostDummy
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.SharedFlow
import kotlin.math.exp

@Composable
fun GuestDetailScreen(
    uiState: PostDetailUiState,
    uiEvent: SharedFlow<DetailUiEvent>,
    snackbarHostState: SnackbarHostState,
    loseLoginInfo: () -> Unit = {},
    navPopBackStack: () -> Unit = {},
    retryAction: () -> Unit = {},
    applyButton: (UserStatus) -> Unit,
    onRefresh: () -> Unit,
    navigateToMange: () -> Unit = {},
    onEdit: () -> Unit = {},
    secessionAction: () -> Unit = {},
    onDelete: () -> Unit = {},
    onDeleteBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(uiEvent) {
        uiEvent.collect { event ->
            when (event) {
                DetailUiEvent.LoseLoginInfo -> {}
                DetailUiEvent.ManageGuest -> navigateToMange()
                DetailUiEvent.DeleteCompletedPost -> onDeleteBack()
                is DetailUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is DetailUiEvent.NoSuchData -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    navPopBackStack()
                }

                DetailUiEvent.PopBackStack -> navPopBackStack()
            }
        }
    }
    when (uiState.dataState) {
        is DataState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is DataState.Success -> GuestDetailScreen(
            guestDetail = uiState.dataState.data,
            statusLoading = uiState.statusLoading,
            navPopBackStack = navPopBackStack,
            applyButton = applyButton,
            onRefresh = onRefresh,
            onEdit = onEdit,
            secessionAction = secessionAction,
            onDelete = onDelete,
            modifier = modifier,
        )

        is DataState.Error -> ErrorScreen(
            retryAction = retryAction,
            errorMessage = uiState.dataState.message,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GuestDetailScreen(
    guestDetail: PostDetailDataState,
    statusLoading: Boolean,
    navPopBackStack: () -> Unit,
    applyButton: (UserStatus) -> Unit = {},
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    secessionAction: () -> Unit = {},
    isDeleteLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isSecessionDialog by remember { mutableStateOf(false) }
    val postDetail = guestDetail.postDetail
    val userData = guestDetail.writerInfo
    val userStatus = guestDetail.myStatus
    Box(modifier = modifier) {
        Column(modifier = Modifier) {
            GuestDetailTopAppBar(
                navPopBackStack = navPopBackStack,
                userStatus = userStatus,
                onEdit = onEdit,
                onDelete = onDelete
            )
            PullToRefreshBox(isRefreshing = false, onRefresh = onRefresh) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WriterInfoSection(
                        userData = userData,
                        userStatus = userStatus,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text = postDetail.title, style = MaterialTheme.typography.titleLarge)
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CalendarMonth, null, tint = Color.LightGray)
                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = postDetail.startDate.toFormattedFilterDate() + " " + startTimeWithEndTime(
                                postDetail.startDate,
                                postDetail.endDate
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PeopleAlt, null, tint = Color.LightGray)
                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "${postDetail.currentMember}/${postDetail.memberCount}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SportsBasketball, null, tint = Color.LightGray)
                        FlowRow(
                            modifier = Modifier.padding(start = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            postDetail.positions.forEach { position ->
                                PositionChip2(
                                    position = Position.fromFirestoreValue(position).labelRes,
                                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow
                                )
                            }
                        }
                    }
                    Text(modifier = Modifier.padding(top = 8.dp), text = postDetail.description)

                    Spacer(modifier = Modifier.height(8.dp))
                    AddressSection(
                        placeName = postDetail.placeName,
                        placeAddress = postDetail.placeAddress,
                        latLng = LatLng(postDetail.lat, postDetail.lng),
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val buttonString = stringResource(GetUserStatusString(userStatus))
            HorizontalDivider()
            DefaultButton(
                buttonString,
                onClick = {
                    if (userStatus == UserStatus.GUEST) {
                        isSecessionDialog = true
                        return@DefaultButton
                    }
                    applyButton(userStatus)
                },
                loading = statusLoading,
                enabled = userStatus != UserStatus.DENIED,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth()
            )
        }
    } // end Box
    if (isSecessionDialog) {
        TextDialog(title = stringResource(R.string.guestsecession),
            message = stringResource(R.string.guestsecessionbody),
            onConfirm = {
                secessionAction()
                isSecessionDialog = false
            },
            onDismiss = { isSecessionDialog = false })
    }
}

@Composable
private fun WriterInfoSection(
    userData: User,
    userStatus: UserStatus,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (userData.profileImageUrl.isEmpty()) {
            DefaultProfileImage(modifier = Modifier.size(36.dp))
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userData.profileImageUrl)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholderimage),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(36.dp)
            )
        }
        Text(
            userData.nickName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (userStatus != UserStatus.OWNER) {
            TextButton(onClick = {}) {
                Text(
                    stringResource(R.string.chat),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun AddressSection(
    placeName: String,
    placeAddress: String,
    latLng: LatLng,
    modifier: Modifier = Modifier
) {
    val mapProperties by remember { mutableStateOf(MapProperties(maxZoom = 18.0, minZoom = 12.0)) }
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(latLng.latitude, latLng.longitude), 15.0)
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = stringResource(R.string.addressInfo),
            style = MaterialTheme.typography.titleMedium
        )
        Row {
            Text(
                text = stringResource(R.string.addressname),
                modifier = Modifier.weight(3f), textAlign = TextAlign.Start,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = placeName, modifier = Modifier.weight(8f), textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row {
            Text(
                text = stringResource(R.string.addressdetail),
                modifier = Modifier.weight(3f), textAlign = TextAlign.Start,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = placeAddress, modifier = Modifier.weight(8f), textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        NaverMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp)),
            properties = mapProperties, cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = latLng),
                captionText = placeName
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun GuestDetailScreenPreview() {
    AppTheme {
        GuestDetailScreen(
            guestDetail =
            PostDetailDataState(
                postDetail = guestPostDummy,
                myStatus = UserStatus.NONE,
                writerInfo = User(nickName = "김동경")

            ), statusLoading = false,
            modifier = Modifier.fillMaxSize(),
            onRefresh = {},
            navPopBackStack = {},
            onEdit = {},
            onDelete = {}
        )
    }
}