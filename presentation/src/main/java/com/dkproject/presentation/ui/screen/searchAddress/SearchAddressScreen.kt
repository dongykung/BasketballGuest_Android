package com.dkproject.presentation.ui.screen.searchAddress

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dkproject.domain.model.Poi
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.TextField.DefaultSearchBar
import com.dkproject.presentation.ui.component.util.EmptyResultScreen
import com.dkproject.presentation.ui.component.util.ErrorScreen
import com.dkproject.presentation.ui.component.util.FooterErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen

@Composable
fun SearchAddressScreen(
    viewModel: SearchAddressViewModel = hiltViewModel(),
    onItemClick: (Poi) -> Unit = {},
    onCancel: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults = viewModel.searchFlow.collectAsLazyPagingItems()
    val hasSearched by viewModel.hasSearched.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.searchplace), modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.bodyMedium)
            IconButton(modifier = Modifier.padding(end = 16.dp).align(Alignment.TopEnd),onClick = onCancel) {
                Icon(Icons.Default.Clear, contentDescription = null)
            }
        }
        DefaultSearchBar(
            text = searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            textStyle = MaterialTheme.typography.bodyMedium,
            placeHolder = stringResource(R.string.searchplace),
            leadingIcon = Icons.Default.Search,
            trailingIcon = Icons.Default.Clear,
            trailingIconClick = { viewModel.updateSearchQuery("") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.performSearch()
                keyboardController?.hide()
            })
        )
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = RectangleShape)

        )
        when (val refreshState = searchResults.loadState.refresh) {
            is LoadState.Error -> {
                ErrorScreen(
                    retryAction = { viewModel.performSearch() },
                    errorMessage = refreshState.error.message.toString(),
                    modifier = Modifier.fillMaxSize()
                )
            }

            is LoadState.Loading -> {
                LoadingScreen(modifier = Modifier.fillMaxSize())
            }
            else -> {
                if (searchResults.itemCount == 0) {
                    EmptyResultScreen(
                        modifier = Modifier.fillMaxSize(),
                        hasSearched = hasSearched,
                        query = searchQuery
                    )
                } else
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(searchResults.itemCount, key = { it }) { index ->
                            searchResults[index]?.run {
                                SearchItem(
                                    item = this,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onItemClick(this) })
                            }
                            if (index < searchResults.itemCount - 1) HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.LightGray,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                        item {
                            when (searchResults.loadState.append) {
                                is LoadState.Error -> {
                                    FooterErrorScreen(modifier = Modifier.fillMaxWidth(), retryAction = {
                                        searchResults.retry()
                                    })
                                }
                                LoadState.Loading -> { LoadingScreen(modifier = Modifier.fillMaxWidth()) }
                                is LoadState.NotLoading -> {}
                            }
                        }
                    }
            }
        }
    }
}