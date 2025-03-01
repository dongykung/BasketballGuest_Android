package com.dkproject.presentation.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.model.Position

@Composable
fun PositionChip2(
    position: Int,
    style: TextStyle = LocalTextStyle.current,
    backgroundColor: Color = Color(0XFFFFF7ED),
    color: Color = MaterialTheme.colorScheme.secondaryContainer
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            text = stringResource(position),
            color = MaterialTheme.colorScheme.secondaryContainer,
            style = style
        )
    }
}