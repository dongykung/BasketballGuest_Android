package com.dkproject.presentation.ui.screen.GuestPost

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.domain.model.Poi
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.component.button.DefaultOutlinedButton
import com.dkproject.presentation.ui.screen.searchAddress.SearchAddressScreen
import com.dkproject.presentation.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestAddressScreen(
    modifier: Modifier = Modifier,
    detailAddress: String,
    loading: Boolean = true,
    onAddressChange: (Poi) -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    var isSearchAddressOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        })
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.address),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(32.dp))
        DefaultOutlinedButton(
            text = detailAddress.ifEmpty { stringResource(R.string.searchplace) },
            onClick = { isSearchAddressOpen = true },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        DefaultButton(
            title = stringResource(R.string.complete),
            onClick = onConfirmClick,
            loading = loading,
            enabled = detailAddress.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
    }
    if (isSearchAddressOpen) {
        ModalBottomSheet(
            modifier = Modifier.statusBarsPadding(),
            onDismissRequest = {},
            sheetState = sheetState,
            dragHandle = {}
        ) {
            SearchAddressScreen(
                onItemClick = {
                    onAddressChange(it)
                    isSearchAddressOpen = false
                },
                onCancel = { isSearchAddressOpen = false }
            )
        }
    }
}
/*.padding(
start = WindowInsets.safeDrawing.asPaddingValues()
.calculateStartPadding(layoutDirection),
end = WindowInsets.safeDrawing.asPaddingValues()
.calculateEndPadding(layoutDirection)*/
@Composable
@Preview(showBackground = true)
private fun GuestAddressScreenPreview() {
    AppTheme {
        GuestAddressScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            detailAddress = ""
        )
    }
}