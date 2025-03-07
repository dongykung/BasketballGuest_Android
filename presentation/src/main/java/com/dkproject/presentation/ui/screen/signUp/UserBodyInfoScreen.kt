package com.dkproject.presentation.ui.screen.signUp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.wheelpicker.IntWheelPicker
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
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
    var isHeightBottomSheet by remember { mutableStateOf(false) }
    var isWeightBottomSheet by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Column(modifier = modifier) {
        Column(modifier = Modifier.verticalScroll(state = scrollState)) {
            Text(
                text = stringResource(R.string.height),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(16.dp), modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isHeightBottomSheet = true },
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                val text =
                    if (height == null) stringResource(R.string.setheight) else height.toString() + "cm"
                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.weight),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(16.dp), modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isWeightBottomSheet = true },
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                val text =
                    if (weight == null) stringResource(R.string.setweight) else weight.toString() + "kg"
                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Info, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.skipexplain),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
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
            enabled = height != null && weight != null,
            modifier = Modifier.fillMaxWidth()
        )
    }
    if (isHeightBottomSheet) {
        ModalBottomSheet(onDismissRequest = { isHeightBottomSheet = false }) {
            IntWheelPicker(currentValue = height ?: 175, list = (140..<210).toList(),
                onConfirmClick = { height ->
                    heightValueChange(height)
                    isHeightBottomSheet = false
                })
        }
    }
    if (isWeightBottomSheet) {
        ModalBottomSheet(onDismissRequest = { isWeightBottomSheet = false }) {
            IntWheelPicker(currentValue = weight ?: 75, list = (50..120).toList(),
                onConfirmClick = { weight ->
                    weightValueChange(weight)
                    isWeightBottomSheet = false
                })
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun UserBodyInfoScreenPreview() {
    AppTheme {
        UserBodyInfoScreen(
            onConfirmClick = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            height = 175,
            weight = 66,
            loading = false
        )
    }
}