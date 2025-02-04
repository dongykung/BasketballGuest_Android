package com.dkproject.presentation.ui.component.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun DefaultButton(
    title: String,
    onClick: () -> Unit,
    loading: Boolean = false,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    textColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = title,
                color = textColor,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            AnimatedVisibility(loading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(start = 16.dp),
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultButtonPreview() {
    AppTheme {
        Column {
            DefaultButton("다음", {}, modifier = Modifier.fillMaxWidth(), loading = false)
        }
    }
}