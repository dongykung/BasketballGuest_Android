package com.dkproject.presentation.ui.component.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R

@Composable
fun FooterErrorScreen(
    modifier: Modifier = Modifier,
    retryAction: () -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            OutlinedButton(onClick = retryAction) {
                Text(text = stringResource(R.string.retry), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}