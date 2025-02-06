package com.dkproject.presentation.ui.component.wheelpicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.ui.theme.AppTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Composable
fun IntWheelPicker(
    modifier: Modifier = Modifier,
    pickerMaxHeight: Dp = 200.dp,
    currentValue: Int,
    list: List<Int>,
    onValueChange: (Int) -> Unit = {}
) {
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = list.size / 2
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        val targetIndex = list.indexOf(currentValue)
        if (targetIndex != -1) {
            lazyListState.animateScrollToItem(targetIndex)
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0XFFF2F2F7))
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(pickerMaxHeight),
            state = lazyListState,
            contentPadding = PaddingValues(16.dp, 80.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            itemsIndexed(list) { index, item ->
                val centerIndex by remember {
                    derivedStateOf {
                        lazyListState.firstVisibleItemIndex
                    }
                }

                val isSelected = index == centerIndex
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
                                lazyListState.animateScrollToItem(index)
                            }
                        }
                ) {
                    Text(
                        text = "$item",
                        color = animatedColor,
                        modifier = Modifier
                            .scale(animatedScale)
                    )
                }
            }
        }
    }
    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { currentIndex ->
                if (currentIndex in list.indices) {
                    onValueChange(list[currentIndex])
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
private fun IntWheelPickerPreview() {
    AppTheme {
        IntWheelPicker(
            list = (100..200).toList(),
            currentValue = 177
        )
    }
}