package com.dkproject.presentation.ui.component.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.extension.toFormattedfullString

@Composable
fun DefaultOutlinedButton(
    onClick: () -> Unit,
    text: String,
    shape: Shape = RoundedCornerShape(16.dp),
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = shape,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}