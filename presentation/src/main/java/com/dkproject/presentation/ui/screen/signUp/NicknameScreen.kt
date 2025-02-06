package com.dkproject.presentation.ui.screen.signUp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.component.TextField.DefaultTextField
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun NicknameScreen(
    nickname: String,
    errorMessage: String,
    loading: Boolean,
    onNicknameChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Text(text = stringResource(R.string.inputnickname),
            style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        DefaultTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            label = stringResource(R.string.nickname),
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedVisibility(errorMessage.isNotEmpty()) {
            Text(text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        DefaultButton(
            title = stringResource(R.string.next),
            loading = loading,
            onClick = onConfirmClick,
            enabled = nickname.isNotEmpty() && errorMessage.isEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NicknameScreenPreview() {
    AppTheme {
        NicknameScreen(
            nickname = "df",
            errorMessage = "",
            loading = false,
            onNicknameChange = {},
            onConfirmClick = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}