package com.dkproject.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.domain.model.UserStatus
import com.dkproject.presentation.ui.theme.AppTheme
import com.dkproject.presentation.util.getGeustStatusColorSet
import com.dkproject.presentation.util.getGuestManageStatusString

@Composable
fun StatusChip(
    status: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(26.dp),
        color = backgroundColor
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun StatusChipPreview() {
    AppTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            val status = UserStatus.APPLY
            val colorSet = getGeustStatusColorSet(status)
            val text = getGuestManageStatusString(status)
            StatusChip(
                status = stringResource(text),
                backgroundColor = colorSet.first,
                textColor = colorSet.second
            )
        }
    }
}