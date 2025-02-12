package com.dkproject.presentation.ui.screen.GuestPost

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.dkproject.presentation.ui.theme.AppTheme
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun GuestPositionScreen(
    modifier: Modifier = Modifier,
    positions: List<String>,
    memberCount: Int,
    memberCountChange: (Int) -> Unit = {},
    onPositionSelected: (Position) -> Unit,
    onConfirmClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.recruitmentposition),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        LazyVerticalGrid(
            GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(Position.entries.toTypedArray()) { position ->
                val animatedColor by animateColorAsState(
                    targetValue = if (positions.contains(position.toFirestoreValue())) Color(0xFFFF8C00 ) else MaterialTheme.colorScheme.inverseSurface,
                    label = "",
                )
                Button(
                    onClick = { onPositionSelected(position) },
                    colors = ButtonDefaults.buttonColors(containerColor = animatedColor)
                ) {
                    Text(
                        text = stringResource(position.labelRes), modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.memberCount),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Slider(
                value = memberCount.toFloat(),
                onValueChange = {
                    val discreteValue = max(it.roundToInt(), 1)
                    memberCountChange(discreteValue)
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.inverseSurface,
                    inactiveTickColor = MaterialTheme.colorScheme.tertiaryContainer,
                    inactiveTrackColor = MaterialTheme.colorScheme.inverseSurface,
                    activeTickColor = MaterialTheme.colorScheme.inverseSurface,
                    activeTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                valueRange = 0f..15f,
                steps = 14,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = memberCount.toString(), style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(32.dp))

        DefaultButton(
            title = stringResource(R.string.next),
            onClick = onConfirmClick ,
            enabled = positions.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GuestPositionScreenPreview() {
    AppTheme {
        GuestPositionScreen(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            positions = emptyList(),
            onPositionSelected = {},
            memberCount = 2
        )
    }
}