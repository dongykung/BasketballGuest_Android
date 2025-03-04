package com.dkproject.presentation.ui.screen.myPage

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dkproject.domain.model.DataState
import com.dkproject.presentation.R
import com.dkproject.presentation.model.Position
import com.dkproject.presentation.ui.component.Image.CustomImage
import com.dkproject.presentation.ui.component.Image.DefaultProfileImage
import com.dkproject.presentation.ui.component.PositionChip2
import com.dkproject.presentation.ui.component.sheet.PositionFilter
import com.dkproject.presentation.ui.component.TextDialog
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.component.sheet.ChangeNicknameSheet
import com.dkproject.presentation.ui.component.util.ErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen
import com.dkproject.presentation.ui.component.wheelpicker.IntWheelPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    moveToLogin: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var isLogoutDialog by remember { mutableStateOf(false) }
    var isEditNickname by remember { mutableStateOf(false) }
    var isEditPosition by remember { mutableStateOf(false) }
    var isEditHeight by remember { mutableStateOf(false) }
    var isEditWeight by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                MyPageUiEvent.NavigateToLogin -> {
                    moveToLogin()
                }

                MyPageUiEvent.CompleteChangeNickname -> isEditNickname = false
                is MyPageUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is MyPageUiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }
    Box(modifier = modifier) {
        Column {
            TopAppBar(title = { Text(text = stringResource(R.string.myInfo)) })
            when (val state = uiState.dataState) {
                is DataState.Error -> ErrorScreen(
                    errorMessage = state.message,
                    retryAction = { viewModel.loadMyData() },
                    modifier = Modifier.fillMaxSize()
                )

                DataState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                is DataState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        MyProfileSection(
                            myProfileUrl = state.data.profileImageUrl,
                            myName = state.data.nickName,
                            onEditNickname = { isEditNickname = true },
                            onUpdateProfileImage = { viewModel.updateProfileImage(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        )

                        MyInfoSection(
                            positions = state.data.position,
                            height = state.data.height,
                            weight = state.data.weight,
                            onEditPosition = { isEditPosition = true },
                            onEditHeight = { isEditHeight = true },
                            onEditWeight = { isEditWeight = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        )

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable { isLogoutDialog = true },
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.AutoMirrored.Default.Logout, null)
                                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                                Text(text = stringResource(R.string.logout))
                            }
                        }
                    }
                }
            }
        }
        if (uiState.isUpdateLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    if (isLogoutDialog) {
        TextDialog(
            title = stringResource(R.string.logout),
            message = stringResource(R.string.logoutmessage),
            onDismiss = { isLogoutDialog = false },
            onConfirm = { moveToLogin() }
        )
    }
    val userData = (uiState.dataState as? DataState.Success)?.data
    if (isEditPosition && userData != null) {
        ModalBottomSheet(onDismissRequest = { isEditPosition = false }) {
            PositionFilter(selectedPosition = userData.position.map { Position.fromFirestoreValue(it) },
                onCancelClick = { isEditPosition = false },
                onConfirmClick = { newList ->
                    isEditPosition = false
                    val newPosition = newList.map { it.toFirestoreValue() }
                    viewModel.updateUserPosition(position = newPosition)
                }
            )
        }
    }
    if (isEditHeight && userData != null) {
        var selectedHeight by remember { mutableIntStateOf(userData.height ?: 175) }
        ModalBottomSheet(onDismissRequest = { isEditHeight = false }) {
            IntWheelPicker(currentValue = userData.height ?: 175, list = (140..<210).toList(),
                onValueChange = { height ->
                    selectedHeight = height
                    isEditHeight = false
                })
            Spacer(modifier = Modifier.height(24.dp))
            DefaultButton(
                title = stringResource(R.string.confirm), onClick = {
                    viewModel.updateUserHeight(selectedHeight)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            DefaultButton(
                title = stringResource(R.string.privateinfo),
                onClick = {
                    viewModel.updateUserHeight(null)
                    isEditHeight = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    if (isEditWeight && userData != null) {
        var selectedWeight by remember { mutableIntStateOf(userData.height ?: 175) }
        ModalBottomSheet(onDismissRequest = { isEditWeight = false }) {
            IntWheelPicker(currentValue = userData.weight ?: 75, list = (50..120).toList(),
                onValueChange = { weight ->
                    selectedWeight = weight
                    isEditWeight = false
                })
            Spacer(modifier = Modifier.height(24.dp))
            DefaultButton(
                title = stringResource(R.string.confirm), onClick = {
                    viewModel.updateUserWeight(selectedWeight)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            DefaultButton(
                title = stringResource(R.string.privateinfo),
                onClick = {
                    viewModel.updateUserWeight(null)
                    isEditWeight = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    if (isEditNickname && userData != null) {
        ModalBottomSheet(onDismissRequest = { isEditNickname = false }) {
            ChangeNicknameSheet(
                nickName = userData.nickName,
                onNicknameChange = { viewModel.updateUserNickname(it) },
                isLoading = uiState.isUpdateLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun MyProfileSection(
    myProfileUrl: String,
    myName: String,
    onEditNickname: () -> Unit = {},
    onUpdateProfileImage: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pickVisualMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onUpdateProfileImage(uri.toString())
            }
        }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            if (myProfileUrl.isEmpty()) {
                DefaultProfileImage(modifier = Modifier.size(96.dp).clickable {
                    pickVisualMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                })
            } else {
                CustomImage(
                    url = myProfileUrl,
                    modifier = Modifier.size(96.dp)
                        .clip(CircleShape)
                        .clickable {
                            pickVisualMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(
                    text = myName,
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = onEditNickname) {
                    Icon(
                        painterResource(R.drawable.ic_edit),
                        null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyInfoSection(
    positions: List<String>,
    height: Int?,
    weight: Int?,
    onEditPosition: () -> Unit = {},
    onEditHeight: () -> Unit = {},
    onEditWeight: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        shadowElevation = 2.dp
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp)
        ) {
            //포지션
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.SportsBasketball,
                    null,
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.userposition),
                        style = MaterialTheme.typography.titleSmall
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        positions.forEach { position ->
                            PositionChip2(position = Position.fromFirestoreValue(position).labelRes)
                        }
                    }
                }
                //Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onEditPosition) {
                    Icon(
                        painterResource(R.drawable.ic_edit),
                        null
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            //신장
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Height,
                    null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(R.string.height),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = if (height == null) "정보 없음" else "$height cm",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onEditHeight) {
                    Icon(
                        painterResource(R.drawable.ic_edit),
                        null
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            // 체중
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccessibilityNew,
                    null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(R.string.weight),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = if (weight == null) "정보 없음" else "$weight kg",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onEditWeight) {
                    Icon(
                        painterResource(R.drawable.ic_edit),
                        null
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MyPageScreenPreview() {
    MyProfileSection(myProfileUrl = "", myName = "동키", modifier = Modifier.fillMaxWidth())
}

@Composable
@Preview(showBackground = true)
private fun MyInfoSectionPreview() {
    MyInfoSection(
        positions = listOf("스몰 포워드", "센터", "슈팅 가드"),
        modifier = Modifier.fillMaxWidth(),
        height = 177,
        weight = 74
    )
}