package com.dkproject.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.domain.model.GuestFilter
import com.dkproject.presentation.R
import com.dkproject.presentation.extension.toFormattedFilterDate
import java.util.Date

@Composable
fun GuestFilterTopBar(
    modifier: Modifier = Modifier,
    guestFilter: GuestFilter
) {
    val positionText = when {
        guestFilter.selectedPosition.isEmpty() ->
            stringResource(R.string.userposition)
        guestFilter.selectedPosition.size == 1 ->
            guestFilter.selectedPosition.first()
        else -> {
            stringResource(R.string.filterposition, guestFilter.selectedPosition.first(), guestFilter.selectedPosition.size - 1)
        }
    }

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = {}) {
            Text(text = stringResource(R.string.nearby))
        }
        OutlinedButton(onClick = {}) {
            Text(
                text = if (guestFilter.selectedDate == null) stringResource(R.string.date)
                else guestFilter.selectedDate?.toFormattedFilterDate() ?: ""
            )
        }
        OutlinedButton(onClick = {}) {
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
        guestFilter = GuestFilter(selectedDate = Date())
    )
}