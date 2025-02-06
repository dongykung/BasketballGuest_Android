package com.dkproject.presentation.ui.screen.signUp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.wheelpicker.IntWheelPicker
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun UserBodyInfoScreen(
    onConfirmClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    height: Int?,
    weight: Int?,
    loading: Boolean,
    heightValueChange: (Int) -> Unit = {},
    weightValueChange: (Int) -> Unit = {},
    onSkip: () -> Unit = {},
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
                onValueChange = heightValueChange,
                currentValue = height ?: 175
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
                onValueChange = weightValueChange,
                currentValue = weight ?: 85
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.padding(bottom = 8.dp),verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Info, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = stringResource(R.string.skipexplain),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 6.dp))
        }
        DefaultButton(
            title = stringResource(R.string.skip),
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
            modifier = Modifier.fillMaxSize().padding(16.dp),
            height = 175,
            weight = 66,
            loading = false
        )
    }
}