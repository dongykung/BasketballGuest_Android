package com.dkproject.presentation.ui.screen.GuestDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dkproject.domain.model.UserStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestDetailTopAppBar(
    navPopBackStack: () -> Unit,
    userStatus: UserStatus,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = navPopBackStack) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    null
                )
            }
        },
        actions = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                // 드롭다운 메뉴를 Box 내부에 배치하여 아이콘 버튼의 위치를 기준으로 표시
                GuestDetailDropdownMenu(
                    expanded = expanded,
                    isOwner = userStatus == UserStatus.OWNER,
                    onEdit = {
                        onEdit()
                        expanded = false
                    },
                    onReport = { /* 신고 처리 */ },
                    onDelete = onDelete,
                    onDismissRequest = { expanded = false },
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}