package com.dkproject.presentation.ui.component.wheelpicker

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

enum class TimeType(@StringRes val text: Int) {
    AM(R.string.am), PM(R.string.pm)
}

@Composable
fun TimeWheelPicker(
    date: Date,
    onConfirmClick: (Date) -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    val coroutineScope = rememberCoroutineScope()
    val timeLazyListState = rememberLazyListState(if (calendar.get(Calendar.AM_PM) == 0) 0 else 1)
    val timeType by remember { derivedStateOf {
        TimeType.entries[timeLazyListState.firstVisibleItemIndex]
        }
    }


    val hourListState = rememberLazyListState(if(calendar.get(Calendar.HOUR) == 0) 11 else calendar.get(Calendar.HOUR) - 1)
    val hour by remember {
        derivedStateOf {
            hourListState.firstVisibleItemIndex + 1
        }
    }
    val minListState = rememberLazyListState(calendar.get(Calendar.MINUTE))
    val min by remember {
        derivedStateOf {
            minListState.firstVisibleItemIndex
        }
    }
    Column {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onCancelClick) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.cancel))
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .drawBehind {
                        drawLine(
                            Color(0xFFD0D0D0),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 0.75.dp.toPx()
                        )
                        drawLine(
                            Color(0xFFD0D0D0),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 0.75.dp.toPx()
                        )
                    }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                LazyColumn(
                    state = timeLazyListState,
                    contentPadding = PaddingValues(16.dp, 80.dp),
                    flingBehavior = rememberSnapFlingBehavior(timeLazyListState),
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                ) {
                    itemsIndexed(TimeType.entries) { index, type ->
                        val isSelected = type == timeType
                        val animatedScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.5f else 1.0f, label = ""
                        )
                        val animatedColor by animateColorAsState(
                            targetValue = if (isSelected) Color.Unspecified else Color.Gray, label = ""
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        timeLazyListState.scrollToItem(index)
                                    }
                                }
                        ) {
                            Text(
                                text = stringResource(type.text),
                                style = MaterialTheme.typography.titleMedium,
                                color = animatedColor,
                                modifier = Modifier.scale(animatedScale)
                            )
                        }
                    }
                }

                LazyColumn(
                    state = hourListState,
                    contentPadding = PaddingValues(16.dp, 80.dp),
                    flingBehavior = rememberSnapFlingBehavior(hourListState),
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                ) {
                    itemsIndexed((1..12).toList()) { index, h ->
                        val isSelected = h == hour
                        val animatedScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.5f else 1.0f, label = ""
                        )
                        val animatedColor by animateColorAsState(
                            targetValue = if (isSelected) Color.Unspecified else Color.Gray, label = ""
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        hourListState.scrollToItem(index)
                                    }
                                }
                        ) {
                            Text(
                                text = stringResource(R.string.hour, h),
                                style = MaterialTheme.typography.titleMedium,
                                color = animatedColor,
                                modifier = Modifier.scale(animatedScale)
                            )
                        }
                    }
                }

                LazyColumn(
                    state = minListState,
                    contentPadding = PaddingValues(16.dp, 80.dp),
                    flingBehavior = rememberSnapFlingBehavior(minListState),
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                ) {
                    itemsIndexed((0..59).toList()) { index, m ->
                        val isSelected = m == min
                        val animatedScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.5f else 1.0f, label = ""
                        )
                        val animatedColor by animateColorAsState(
                            targetValue = if (isSelected) Color.Unspecified else Color.Gray, label = ""
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        minListState.scrollToItem(index)
                                    }
                                }
                        ) {
                            Text(
                                text = stringResource(R.string.min, m),
                                style = MaterialTheme.typography.titleMedium,
                                color = animatedColor,
                                modifier = Modifier.scale(animatedScale)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        DefaultButton(
            title = stringResource(R.string.confirm),
            onClick = {
                val adjustedHour = if (hour == 12) 0 else hour
                val cal = Calendar.getInstance().apply {
                    time = date
                    set(Calendar.AM_PM, if (timeType == TimeType.AM) Calendar.AM else Calendar.PM)
                    set(Calendar.HOUR, adjustedHour)
                    set(Calendar.MINUTE,min)
                }
                onConfirmClick(cal.time)
            },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun TimeWheelPickerPreview() {
    AppTheme {
        val calendar = Calendar.getInstance().apply {
            time = Date()
            set(Calendar.HOUR, 24)
            set(Calendar.MINUTE, 55)
        }
        TimeWheelPicker(date = calendar.time)
    }
}
