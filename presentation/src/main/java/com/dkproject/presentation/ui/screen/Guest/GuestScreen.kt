package com.dkproject.presentation.ui.screen.Guest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dkproject.presentation.extension.startTimeWithEndTime
import com.dkproject.presentation.extension.toFormattedHomeGuestListString
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.model.Position
import com.dkproject.presentation.ui.component.GuestFilterTopBar
import com.dkproject.presentation.ui.component.util.EmptyGuestScreen
import com.dkproject.presentation.ui.component.util.ErrorScreen
import com.dkproject.presentation.ui.component.util.FooterErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen
import com.dkproject.presentation.ui.theme.AppTheme
import com.dkproject.presentation.util.guestPostDummy

@Composable
fun GuestScreen(
    uiState: GuestListUiState,
    modifier: Modifier = Modifier,
) {
    val postLists = uiState.guestList.collectAsLazyPagingItems()

    Column(modifier = modifier) {
        GuestFilterTopBar(
            guestFilter = uiState.guestFilter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        )
        when (val refreshState = postLists.loadState.refresh) {
            is LoadState.Error -> {
                ErrorScreen(
                    retryAction = { postLists.retry() },
                    errorMessage = refreshState.error.message.toString(),
                    modifier = Modifier.fillMaxSize()
                )
            }

            is LoadState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is LoadState.NotLoading -> {
                if (postLists.itemCount == 0) {
                    EmptyGuestScreen(modifier = Modifier.fillMaxSize())
                } else
                    LazyColumn {
                        items(postLists.itemCount) { index ->
                            postLists[index]?.run {
                                GuestListItem(
                                    guestPostUiModel = this,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                        item {
                            when (postLists.loadState.append) {
                                is LoadState.Error -> {
                                    FooterErrorScreen(
                                        modifier = Modifier.fillMaxWidth(),
                                        retryAction = {
                                            postLists.retry()
                                        })
                                }

                                is LoadState.Loading -> {
                                    LoadingScreen(modifier = Modifier.fillMaxWidth())
                                }

                                is LoadState.NotLoading -> {}
                            }
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GuestListItem(
    guestPostUiModel: GuestPostUiModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = guestPostUiModel.title,
                style = MaterialTheme.typography.titleLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = guestPostUiModel.date.toFormattedHomeGuestListString(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = startTimeWithEndTime(
                        guestPostUiModel.startDate,
                        guestPostUiModel.endDate
                    ), style = MaterialTheme.typography.bodySmall
                )
            }
            Text(text = guestPostUiModel.placeName)

            Text(text = guestPostUiModel.placeAddress)

            FlowRow {
                guestPostUiModel.positions.forEach { position ->
                    val positionInt = Position.fromFirestoreValue(value = position)
                    SuggestionChip(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = {},
                        enabled = false,
                        shape = RoundedCornerShape(16.dp),
                        label = { Text(text = stringResource(positionInt.labelRes)) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GuestItemPreivew() {
    AppTheme {
        GuestListItem(
            guestPostUiModel = guestPostDummy, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}