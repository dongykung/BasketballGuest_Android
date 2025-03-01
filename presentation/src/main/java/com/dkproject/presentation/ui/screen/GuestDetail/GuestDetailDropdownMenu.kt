package com.dkproject.presentation.ui.screen.GuestDetail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dkproject.presentation.R

@Composable
fun GuestDetailDropdownMenu(
    expanded: Boolean,
    isOwner: Boolean,
    onEdit: () -> Unit = {},
    onReport: () -> Unit = {},
    onDelete: () -> Unit = {},
    onDismissRequest: () -> Unit,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        if (isOwner)
            DropdownMenuItem(text = { Text(text = stringResource(R.string.edit)) },
                onClick = onEdit,
                leadingIcon = { Icon(Icons.Default.ModeEdit, null) }
            )
        if (!isOwner)
            DropdownMenuItem(text = { Text(text = stringResource(R.string.report)) },
                onClick = onReport,
                leadingIcon = { Icon(Icons.Default.Report, null) })
        if (isOwner)
            DropdownMenuItem(text = { Text(text = stringResource(R.string.delete)) },
                onClick = onDelete,
                leadingIcon = { Icon(Icons.Default.Delete, null) })
    }
}