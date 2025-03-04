package com.dkproject.presentation.ui.component.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun DefaultProfileImage(modifier: Modifier = Modifier) {
    Surface(shape = CircleShape, modifier = modifier.clip(CircleShape).background(Color.Gray)) {
        Icon(Icons.Default.Person, null, tint = Color.Gray)
    }
}

@Composable
@Preview(showBackground = true)
private fun DefaultProfileImagePreview() {
    AppTheme {
        DefaultProfileImage()
    }
}