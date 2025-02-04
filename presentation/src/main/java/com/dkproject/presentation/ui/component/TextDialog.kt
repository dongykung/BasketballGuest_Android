package com.dkproject.presentation.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dkproject.presentation.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
           TextButton(onClick = onDismiss) {
               Text(stringResource(R.string.cancel))
           }
        },
    )
}