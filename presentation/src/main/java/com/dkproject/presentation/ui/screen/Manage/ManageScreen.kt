package com.dkproject.presentation.ui.screen.Manage

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.dkproject.presentation.R
import com.dkproject.presentation.model.GuestPostUiModel

enum class ManageType(@StringRes val title: Int) {
    MYPOST(title = R.string.mypost),
    MYAPPLY(title = R.string.myapply),
}

@Composable
fun ManageScreen(
    uiState: ManageUiState,
    onNavigateToDetail: (GuestPostUiModel) -> Unit,
    onRefreshMyPost: () -> Unit = {},
    onRefreshMyParticipant: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val myPostList = uiState.myPosts.collectAsLazyPagingItems()
    val myParticipantList = uiState.myParticipants.collectAsLazyPagingItems()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabListItem = listOf(ManageType.MYPOST, ManageType.MYAPPLY)
    Column(modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)) {
        TabRow(
            selectedTabIndex = selectedIndex,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        ) {
            tabListItem.forEachIndexed { index, item ->
                val headerText = stringResource(item.title)
                Tab(selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    text = {
                        Text(
                            text = headerText,
                            color = if (selectedIndex == index) MaterialTheme.colorScheme.secondaryContainer else Color.Gray,
                            modifier = Modifier.padding(6.dp)
                        )
                    })
            }
        }
        when (selectedIndex) {
            0 -> MyPostScreen(
                myPostList = myPostList,
                isRefresh = uiState.isMyPostRefresh,
                onNavigateToDetail = onNavigateToDetail,
                onRefreshMyPost = onRefreshMyPost
            )
            1 -> MyApplyScreen(
                myParticipantList = myParticipantList,
                isRefresh =  uiState.isMyParticipantRefresh,
                onNavigateToDetail = onNavigateToDetail,
                onRefreshMyParticipant = onRefreshMyParticipant
            )
        }
    }
}