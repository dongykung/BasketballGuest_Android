package com.dkproject.presentation.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.model.Position
import com.dkproject.presentation.ui.component.button.DefaultButton

@Composable
fun PositionFilter(
    selectedPosition: List<Position>,
    onCancelClick: () -> Unit = {},
    onConfirmClick: (List<Position>) -> Unit = {}
) {
    val newSelectedPositions = remember { mutableStateListOf<Position>() }
    LaunchedEffect(Unit) {
        newSelectedPositions.addAll(selectedPosition)
    }
    Column {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onCancelClick) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cancel))
            }
        }
        LazyVerticalGrid(
            GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            items((Position.entries - Position.NONE).toTypedArray() ) { position ->
                val animatedColor by animateColorAsState(
                    targetValue = if (newSelectedPositions.contains(position)) Color(0xFFFF8C00 ) else MaterialTheme.colorScheme.inverseSurface,
                    label = "",
                )
                Button(
                    onClick = {
                        if (newSelectedPositions.contains(position)) {
                            newSelectedPositions.remove(position)
                        } else {
                            newSelectedPositions.add(position)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = animatedColor)
                ) {
                    Text(
                        text = stringResource(position.labelRes), modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        DefaultButton(
            title = stringResource(R.string.confirm),
            onClick = {
                onConfirmClick(newSelectedPositions)
            },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}