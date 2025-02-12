package com.dkproject.presentation.ui.component.util

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dkproject.presentation.R

@Composable
fun EmptyGuestScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(text = stringResource(R.string.emptyguest))
    }
}