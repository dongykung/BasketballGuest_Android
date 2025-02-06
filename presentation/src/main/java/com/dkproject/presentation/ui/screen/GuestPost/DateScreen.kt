package com.dkproject.presentation.ui.screen.GuestPost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.extension.toFormattedHourAndMinute
import com.dkproject.presentation.extension.toFormattedfullString
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.component.button.DefaultOutlinedButton
import com.dkproject.presentation.ui.component.wheelpicker.DateWheelPicker
import com.dkproject.presentation.ui.component.wheelpicker.TimeWheelPicker
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateScreen(
    date: Date,
    startDate: Date,
    endDate: Date,
    onDateChange: (Date) -> Unit,
    onStartDateChange: (Date) -> Unit,
    onEndDateChange: (Date) -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDatePickerOpen by remember { mutableStateOf(false) }
    var isTimePickerOpen by remember { mutableStateOf(false) }
    var isStart by remember { mutableStateOf(true) }
    val dateBottomSheetState = rememberModalBottomSheetState(
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    val timeBottomSheetState = rememberModalBottomSheetState(
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.date),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(4.dp))

        DefaultOutlinedButton(
            onClick = { isDatePickerOpen = !isDatePickerOpen },
            text = date.toFormattedfullString(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Text(
                text = stringResource(R.string.starttime),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.endtime),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 6.dp)) {
            DefaultOutlinedButton(
                text = startDate.toFormattedHourAndMinute(),
                onClick = {
                    isStart = true
                    isTimePickerOpen = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(text = " - ", modifier = Modifier.padding(horizontal = 8.dp))
            DefaultOutlinedButton(
                text = endDate.toFormattedHourAndMinute(),
                onClick = {
                    isStart = false
                    isTimePickerOpen = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }


        Spacer(modifier = Modifier.height(32.dp))
        AnimatedVisibility(startDate > endDate) {
            Text(
                text = stringResource(R.string.timeerror),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
        DefaultButton(
            title = stringResource(R.string.next),
            onClick = onConfirmClick,
            enabled = startDate < endDate,
            modifier = Modifier.fillMaxWidth()
        )
        if (isDatePickerOpen) {
            ModalBottomSheet(sheetState = dateBottomSheetState,
                onDismissRequest = { isDatePickerOpen = false },
                dragHandle = {}) {
                DateWheelPicker(date = date, onConfirmClick = { selectedDate ->
                    isDatePickerOpen = false
                    onDateChange(selectedDate)
                },
                    onCancelClick = { isDatePickerOpen = false })
            }
        }
        if (isTimePickerOpen) {
            ModalBottomSheet(sheetState = timeBottomSheetState, onDismissRequest = {},
                dragHandle = {}) {
                TimeWheelPicker(
                    if (isStart) startDate else endDate,
                    onConfirmClick = {
                        if (isStart) onStartDateChange(it) else onEndDateChange(it)
                        isTimePickerOpen = false
                    },
                    onCancelClick = { isTimePickerOpen = false }
                )
            }
        }
    }
}