package com.dkproject.presentation.ui.screen.signUp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.IntWheelPicker
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun UserBodyInfoScreen(
    onConfirmClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    heightValueChange: (Int) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier) {
        Column(modifier = Modifier.verticalScroll(state = scrollState)) {
            Text(
                text = stringResource(R.string.height),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            IntWheelPicker(
                modifier = Modifier.fillMaxWidth(),
                list = (140..210).toList(),
                onValueChange = heightValueChange
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.weight),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            IntWheelPicker(
                modifier = Modifier.fillMaxWidth(),
                list = (50..120).toList(),
                onValueChange = heightValueChange
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        DefaultButton(
            title = stringResource(R.string.skip),
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        DefaultButton(
            title = stringResource(R.string.next),
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun UserBodyInfoScreenPreview() {
    AppTheme {
        UserBodyInfoScreen(
            onConfirmClick = {},
            modifier = Modifier.fillMaxSize().padding(16.dp)
        )
    }
}