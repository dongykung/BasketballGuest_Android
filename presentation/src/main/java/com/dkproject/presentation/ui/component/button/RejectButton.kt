package com.dkproject.presentation.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun RejectButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = stringResource(R.string.denied),
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFFEF2F2)),
        onClick = onClick,
        enabled = enabled
    ) {
        Text(
            text = text, color = Color(0XFFEF4444),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecjectButtonPreview() {
    AppTheme {
        RejectButton(onClick = {})
    }
}