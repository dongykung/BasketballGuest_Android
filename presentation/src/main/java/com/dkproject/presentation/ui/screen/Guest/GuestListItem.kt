package com.dkproject.presentation.ui.screen.Guest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.extension.startTimeWithEndTime
import com.dkproject.presentation.extension.toFormattedHomeGuestListString
import com.dkproject.presentation.model.GuestPostUiModel
import com.dkproject.presentation.model.Position


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GuestListItem(
    guestPostUiModel: GuestPostUiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = guestPostUiModel.title,
                style = MaterialTheme.typography.titleLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier,
                    text = guestPostUiModel.date.toFormattedHomeGuestListString(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = startTimeWithEndTime(
                        guestPostUiModel.startDate,
                        guestPostUiModel.endDate
                    ), style = MaterialTheme.typography.bodySmall
                )
            }
            Text(text = guestPostUiModel.placeName, style = MaterialTheme.typography.titleSmall)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn, null,
                    tint = Color.Gray
                )
                Text(
                    text = guestPostUiModel.placeAddress,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                guestPostUiModel.positions.forEach { position ->
                    val positionInt = Position.fromFirestoreValue(value = position)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            text = stringResource(positionInt.labelRes),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}