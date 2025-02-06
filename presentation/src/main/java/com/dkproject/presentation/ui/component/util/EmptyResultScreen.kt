package com.dkproject.presentation.ui.component.util

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.dkproject.presentation.R

@Composable
fun EmptyResultScreen(
    modifier: Modifier = Modifier,
    hasSearched: Boolean,
    query: String,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (query.isBlank() || !hasSearched) {
            Text(text = stringResource(R.string.searchplaceexplain),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(text = stringResource(R.string.no_result, query),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}