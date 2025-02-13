package com.dkproject.presentation.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.domain.model.GuestFilter
import com.dkproject.presentation.R
import com.dkproject.presentation.extension.toFormattedFilterDate
import com.dkproject.presentation.model.GuestFilterUiModel
import java.util.Date

@Composable
fun GuestFilterTopBar(
    modifier: Modifier = Modifier,
    guestFilter: GuestFilterUiModel,
    onNearByClick: () -> Unit = {},
    onDateClick: () -> Unit = {},
    onPositionClick: () -> Unit = {}
) {
    val positionText = when {
        guestFilter.selectedPosition.isEmpty() ->
            stringResource(R.string.userposition)
        guestFilter.selectedPosition.size == 1 ->
            stringResource(guestFilter.selectedPosition.first().labelRes)
        else -> {
            stringResource(R.string.filterposition, stringResource(guestFilter.selectedPosition.first().labelRes), guestFilter.selectedPosition.size - 1)
        }
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        val animatedColor by animateColorAsState(if(guestFilter.isNearBy) MaterialTheme.colorScheme.secondaryContainer else Color.Unspecified,
            label = ""
        )
        OutlinedButton(onClick = onNearByClick, colors = ButtonDefaults.outlinedButtonColors(containerColor = animatedColor)) {
            Text(text = stringResource(R.string.nearby))
        }
        OutlinedButton(onClick = onDateClick) {
            Text(
                text = if (guestFilter.selectedDate == null) stringResource(R.string.date)
                else guestFilter.selectedDate?.toFormattedFilterDate() ?: ""
            )
        }
        OutlinedButton(onClick = onPositionClick) {
            Text(
                text = positionText
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun GuestFilterTopBarPreview() {
    GuestFilterTopBar(
        modifier = Modifier.fillMaxWidth(),
        guestFilter = GuestFilterUiModel(selectedDate = Date())
    )
}