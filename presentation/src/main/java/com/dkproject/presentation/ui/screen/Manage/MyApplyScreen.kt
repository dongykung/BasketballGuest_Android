package com.dkproject.presentation.ui.screen.Manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dkproject.domain.model.MyParticipant
import com.dkproject.presentation.R
import com.dkproject.presentation.extension.startTimeWithEndTime
import com.dkproject.presentation.extension.toFormattedHomeGuestListString
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.model.toUiModel
import com.dkproject.presentation.ui.component.StatusChip
import com.dkproject.presentation.ui.component.util.ErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen
import com.dkproject.presentation.util.getGeustStatusColorSet
import com.dkproject.presentation.util.getGuestManageStatusString

@Composable
fun MyApplyScreen(
    myParticipantList: LazyPagingItems<MyParticipant>,
    isRefresh: Boolean,
    onRefreshMyParticipant: () -> Unit = {},
    onNavigateToDetail: (GuestPostUiModel) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (myParticipantList.loadState.refresh) {
            is LoadState.Error -> ErrorScreen(modifier = Modifier.fillMaxSize(),
                errorMessage = stringResource(R.string.defaulterror),
                retryAction = { myParticipantList.retry() }
            )
            LoadState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is LoadState.NotLoading -> MyParticipantContent(
                myParticipantList = myParticipantList,
                isRefresh = isRefresh,
                onRefreshMyParticipant = onRefreshMyParticipant,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyParticipantContent(
    myParticipantList: LazyPagingItems<MyParticipant>,
    isRefresh: Boolean,
    onRefreshMyParticipant: () -> Unit = {},
    onNavigateToDetail: (GuestPostUiModel) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        PullToRefreshBox(isRefreshing = isRefresh, onRefresh = onRefreshMyParticipant) {
            LazyColumn {
                items(myParticipantList.itemCount) { index ->
                    myParticipantList[index]?.let { participant ->
                        ParticipantItem(
                            participant = participant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .clickable { onNavigateToDetail(participant.post.toUiModel()) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ParticipantItem(
    participant: MyParticipant,
    modifier: Modifier = Modifier,
) {
    val statusColorSet = getGeustStatusColorSet(participant.myStatus)
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = participant.post.title,
                style = MaterialTheme.typography.titleLarge,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier,
                    text = participant.post.date.toFormattedHomeGuestListString(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = startTimeWithEndTime(
                        participant.post.startDate,
                        participant.post.endDate
                    ), style = MaterialTheme.typography.bodySmall
                )
            }
            Text(text = participant.post.placeName, style = MaterialTheme.typography.titleSmall)
            Text(
                text = participant.post.placeAddress,
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            StatusChip(
                status = stringResource(getGuestManageStatusString(participant.myStatus)),
                backgroundColor = statusColorSet.first,
                textColor = statusColorSet.second
            )
        }
    }
}