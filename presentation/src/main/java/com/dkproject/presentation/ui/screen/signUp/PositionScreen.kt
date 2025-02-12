package com.dkproject.presentation.ui.screen.signUp

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.model.Position
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.screen.GuestPost.GuestPostStep
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun PositionScreen(
    positions: List<String>,
    positionTap: (Position) -> Unit = {},
    loading: Boolean = false,
    onConfirmClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.selectposition),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items((Position.entries - Position.NONE).toTypedArray() ) { position ->
                val animatedColor by animateColorAsState(
                    targetValue = if (positions.contains(position.toFirestoreValue())) Color(0xFFFF8C00 ) else MaterialTheme.colorScheme.inverseSurface,
                    label = "",
                )
                Button(
                    onClick = { positionTap(position) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = animatedColor
                    )
                ) {
                    Text(
                        text = stringResource(position.labelRes), modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        DefaultButton(
            title = stringResource(R.string.complete),
            onClick = onConfirmClick,
            enabled = positions.isNotEmpty(),
            loading = loading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PositionScreenPreview() {
    AppTheme {
        PositionScreen(
            positions = emptyList(),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}