package com.dkproject.presentation.ui.screen.GuestManage

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dkproject.domain.model.GuestManage
import com.dkproject.domain.model.User
import com.dkproject.domain.model.UserStatus
import com.dkproject.presentation.R
import com.dkproject.presentation.model.Position
import com.dkproject.presentation.navigation.DefaultCenterTopBar
import com.dkproject.presentation.ui.component.Image.DefaultProfileImage
import com.dkproject.presentation.ui.component.PositionChip2
import com.dkproject.presentation.ui.component.TextDialog
import com.dkproject.presentation.ui.component.button.AcceptButton
import com.dkproject.presentation.ui.component.button.RejectButton
import com.dkproject.presentation.ui.component.util.ErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen
import com.dkproject.presentation.util.getGuestManageStatusString
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun GuestManageScreen(
    uiState: GuestManageUiState,
    uiEvent: SharedFlow<GuestManageUiEvent>,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onAcceptClick: (String) -> Unit = {},
    onRejectClick: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val guestManageList = uiState.guestManageList.collectAsLazyPagingItems()
    val changeStatusItems = uiState.changeStatusItems
    val isRefreshLoading = uiState.isRefreshLoading

    LaunchedEffect(uiEvent) {
        uiEvent.collect { event ->
            when (event) {
                is GuestManageUiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    when (val refreshState = guestManageList.loadState.refresh) {
        is LoadState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is LoadState.Error -> ErrorScreen(
            retryAction = {},
            errorMessage = refreshState.error.message ?: "오류 발생",
            modifier = Modifier.fillMaxSize()
        )

        is LoadState.NotLoading -> GuestManageList(
            guestManageList = guestManageList,
            changeStatusItems = changeStatusItems,
            isLoading = uiState.isLoading,
            isRefreshLoading = isRefreshLoading,
            onBackClick = onBackClick,
            onAcceptClick = onAcceptClick,
            onRejectClick = onRejectClick,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestManageList(
    guestManageList: LazyPagingItems<GuestManage>,
    changeStatusItems: Map<String, UserStatus>,
    isLoading: Boolean,
    isRefreshLoading: Boolean,
    onBackClick: () -> Unit,
    onAcceptClick: (String) -> Unit = {},
    onRejectClick: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var guestForApproval by remember { mutableStateOf<GuestManage?>(null) }
    var guestForRejection by remember { mutableStateOf<GuestManage?>(null) }
    var guestForEmission by remember { mutableStateOf<GuestManage?>(null) }

    val listItem = guestManageList.itemSnapshotList.items.map {
        it.copy(
            userStatus = changeStatusItems[it.user.id] ?: it.userStatus
        )
    }
    val validStatuses = listOf(UserStatus.APPLY, UserStatus.GUEST, UserStatus.DENIED)
    var selectedIndex by remember { mutableIntStateOf(0) }
    val groupByListItem = listItem.filter {
        it.userStatus in listOf(
            UserStatus.APPLY,
            UserStatus.GUEST,
            UserStatus.DENIED
        )
    }.groupBy { it.userStatus }


    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        Column() {
            DefaultCenterTopBar(title = stringResource(R.string.manage), onBackClick = onBackClick)
            PullToRefreshBox(
                isRefreshing = isRefreshLoading,
                onRefresh = onRefresh
            ) {
                Column {
                    TabRow(
                        selectedTabIndex = selectedIndex,
                        indicator = { tabPositions ->
                            SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                        }
                    ) {
                        validStatuses.forEachIndexed { index, userStatus ->
                            val headerText = stringResource(getGuestManageStatusString(userStatus))
                            val manageList = groupByListItem[userStatus] ?: emptyList()
                            Tab(selected = selectedIndex == index,
                                onClick = { selectedIndex = index },
                                text = {
                                    Text(
                                        text = "${headerText}(${manageList.size})",
                                        color = if (selectedIndex == index) MaterialTheme.colorScheme.secondaryContainer else Color.Gray
                                    )
                                }
                            )
                        }
                    }
                    AnimatedContent(targetState = selectedIndex, label = "") { index ->
                        val manageList = groupByListItem[validStatuses[index]] ?: emptyList()
                        LazyColumn(modifier = Modifier.animateContentSize()) {
                            items(manageList) { item ->
                                GuestManageItem(
                                    user = item.user,
                                    userStatus = item.userStatus,
                                    isLoading = isLoading,
                                    onAcceptClick = { guestForApproval = item },
                                    onRejectClick = { guestForRejection = item },
                                    onEmissionClick = { guestForEmission = item },
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 12.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        if(isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    guestForApproval?.let {
        TextDialog(title = stringResource(R.string.guestAccept),
            message = stringResource(R.string.questionaccept, it.user.nickName),
            onConfirm = {
                onAcceptClick(it.user.id)
                guestForApproval = null
            },
            onDismiss = { guestForApproval = null })
    }
    guestForRejection?.let {
        TextDialog(title = stringResource(R.string.guestreject),
            message = stringResource(R.string.questionreject, it.user.nickName),
            onConfirm = {
                onRejectClick(it.user.id)
                guestForRejection = null
            },
            onDismiss = { guestForRejection = null })
    }
    guestForEmission?.let {
        TextDialog(title = stringResource(R.string.guestemission),
            message = stringResource(R.string.questionemission, it.user.nickName),
            onConfirm = {
                onRejectClick(it.user.id)
                guestForEmission = null
            },
            onDismiss = { guestForEmission = null }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GuestManageItem(
    user: User,
    userStatus: UserStatus,
    isLoading: Boolean,
    onAcceptClick: () -> Unit = {},
    onRejectClick: () -> Unit = {},
    onEmissionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                if (user.profileImageUrl.isEmpty()) {
                    DefaultProfileImage(modifier = Modifier.size(36.dp))
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.profileImageUrl)
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
                Column {
                    Text(
                        text = user.nickName,
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (user.height != null || user.weight != null) {
                        Text(
                            text = "(${user.height}cm / ${user.weight}kg)",
                            modifier = Modifier.padding(start = 12.dp, top = 2.dp),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                when (userStatus) {
                    UserStatus.GUEST -> RejectButton(
                        onClick = onEmissionClick,
                        text = stringResource(R.string.emission),
                        enabled = !isLoading
                    )
                    UserStatus.APPLY -> {
                        GuestManageGuestButton(
                            isLoading = isLoading,
                            onAcceptClick = onAcceptClick,
                            onRejectClick = onRejectClick
                        )
                    }
                    UserStatus.DENIED -> AcceptButton(onClick = onAcceptClick, enabled = !isLoading)
                    else -> {}
                }
            }
            FlowRow(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            {
                user.position.forEach { position ->
                    PositionChip2(
                        position = Position.fromFirestoreValue(position).labelRes,
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                }
            }
        }
    }
}

@Composable
fun GuestManageGuestButton(
    isLoading: Boolean,
    onAcceptClick: () -> Unit = {},
    onRejectClick: () -> Unit = {},
) {
    Row {
        AcceptButton(onClick = onAcceptClick, enabled = !isLoading)
        Spacer(modifier = Modifier.width(3.dp))
        RejectButton(onClick = onRejectClick, enabled = !isLoading)
    }
}


@Composable
fun GuestManageDeniedButton() {

}

