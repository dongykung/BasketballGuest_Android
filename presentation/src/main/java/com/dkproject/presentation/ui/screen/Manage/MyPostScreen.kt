package com.dkproject.presentation.ui.screen.Manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dkproject.presentation.R
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.ui.component.util.ErrorScreen
import com.dkproject.presentation.ui.component.util.FooterErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen
import com.dkproject.presentation.ui.screen.Guest.GuestListItem

@Composable
fun MyPostScreen(
    myPostList: LazyPagingItems<GuestPostUiModel>,
    onNavigateToDetail: (GuestPostUiModel) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (myPostList.loadState.refresh) {
            is LoadState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize(),
                errorMessage = stringResource(R.string.defaulterror),
                retryAction = { myPostList.retry() }
            )
            LoadState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is LoadState.NotLoading -> MyPostContent(myPostList = myPostList, onNavigateToDetail = onNavigateToDetail)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostContent(
    myPostList: LazyPagingItems<GuestPostUiModel>,
    onNavigateToDetail: (GuestPostUiModel) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        PullToRefreshBox(isRefreshing = false, onRefresh = {}) {
            LazyColumn {
                items(myPostList.itemCount) { index ->
                    myPostList[index]?.let { myPost ->
                        GuestListItem(
                            guestPostUiModel = myPost,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .clickable { onNavigateToDetail(myPost) }
                        )
                    }
                }
                item {
                    when(myPostList.loadState.append) {
                        is LoadState.Error -> FooterErrorScreen(modifier = Modifier.fillMaxWidth()) { myPostList.retry() }
                        LoadState.Loading -> LoadingScreen(modifier = Modifier.fillMaxWidth())
                        is LoadState.NotLoading -> {}
                    }
                }
            }
        }
    }
}