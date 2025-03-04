package com.dkproject.presentation.ui.component.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.TextField.DefaultTextField
import com.dkproject.presentation.ui.component.button.DefaultButton

@Composable
fun ChangeNicknameSheet(
    nickName: String,
    onNicknameChange: (String) -> Unit = {},
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(nickName) }
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.changenickname),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        DefaultTextField(
            value = name, onValueChange = { name = it }, label = stringResource(R.string.nickname),
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedVisibility(name.length > 8 || name.length < 2) {
            Text(text = stringResource(R.string.nicknamelengtherror))
        }
        Spacer(modifier = Modifier.height(24.dp))
        DefaultButton(
            title = stringResource(R.string.complete), onClick = { onNicknameChange(name) },
            enabled = name.length in 2..8 && nickName != name, modifier = Modifier.fillMaxWidth(), loading = isLoading
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChangeNickNameSheetPreview() {
    ChangeNicknameSheet(nickName = "동키")
}