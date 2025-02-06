package com.dkproject.presentation.ui.component.wheelpicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

@Composable
fun DateWheelPicker(
    date: Date,
    onConfirmClick: (Date) -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    val coroutineScope = rememberCoroutineScope()

    val yearListState = rememberLazyListState(calendar.get(Calendar.YEAR) - 2025)
    val monthListState = rememberLazyListState(calendar.get(Calendar.MONTH))
    val dayListState = rememberLazyListState(calendar.get(Calendar.DAY_OF_MONTH) - 1)
    
    val year by remember { derivedStateOf { yearListState.firstVisibleItemIndex + 2025 } }
    val month by remember { derivedStateOf { monthListState.firstVisibleItemIndex + 1} }
    val day by remember { derivedStateOf { dayListState.firstVisibleItemIndex + 1 } }
    val daysIn by remember {
        derivedStateOf {
            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
            }
            cal.getActualMaximum(Calendar.DAY_OF_MONTH)
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
                    state = yearListState,
                    contentPadding = PaddingValues(16.dp, 80.dp),
                    flingBehavior = rememberSnapFlingBehavior(yearListState),
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                ) {
                    itemsIndexed((2025..2100).toList()) { index, int ->
                        val animatedColor by animateColorAsState(
                            targetValue = if (year == int) Color.Unspecified else Color(0xFFD0D0D0), label = ""
                        )
                        val animatedScale by animateFloatAsState(
                            targetValue = if (year == int) 1.15f else 1.0f, label = ""
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        yearListState.scrollToItem(index)
                                    }
                                }
                        ) {
                            Text(
                                text = "${int}년",
                                style = MaterialTheme.typography.titleMedium,
                                color = animatedColor,
                                modifier = Modifier.scale(animatedScale)
                            )
                        }
                    }
                }
                LazyColumn(
                    state = monthListState,
                    contentPadding = PaddingValues(16.dp, 80.dp),
                    flingBehavior = rememberSnapFlingBehavior(monthListState),
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                ) {
                    itemsIndexed((1..12).toList()) { index, int ->
                        val animatedColor by animateColorAsState(
                            targetValue = if (month == int) Color.Unspecified else Color(0xFFD0D0D0), label = ""
                        )
                        val animatedScale by animateFloatAsState(
                            targetValue = if (month == int) 1.15f else 1.0f, label = ""
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        monthListState.scrollToItem(index)
                                    }
                                }
                        ) {
                            Text(
                                text = "${int}월",
                                style = MaterialTheme.typography.titleMedium,
                                color = animatedColor,
                                modifier = Modifier.scale(animatedScale)
                            )
                        }
                    }
                }
                LazyColumn(
                    state = dayListState,
                    contentPadding = PaddingValues(16.dp, 80.dp),
                    flingBehavior = rememberSnapFlingBehavior(dayListState),
                    modifier = Modifier
                        .weight(1f)
                        .height(200.dp)
                ) {
                    itemsIndexed((1..daysIn).toList()) { index,int ->
                        val animatedColor by animateColorAsState(
                            targetValue = if (day == int) Color.Unspecified else Color(0xFFD0D0D0), label = ""
                        )
                        val animatedScale by animateFloatAsState(
                            targetValue = if (day == int) 1.15f else 1.0f, label = ""
                        )
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable { 
                                    coroutineScope.launch {
                                        dayListState.scrollToItem(index)
                                    }
                                }
                        ) {
                            Text(
                                text = "${int}일",
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
                val cal = Calendar.getInstance().apply {
                    set(year, month - 1, day)
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
private fun DateWheelPickerPreview() {
    AppTheme {
        val calendar = Calendar.getInstance().apply {
            time = Date()
            set(Calendar.YEAR, 2025)
            set(Calendar.MONTH, 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        Column(modifier = Modifier.fillMaxSize()) {
        DateWheelPicker(date = calendar.time)
            }
    }
}
