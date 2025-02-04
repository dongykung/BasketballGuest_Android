package com.dkproject.presentation.ui.screen.GuestPost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.component.DefaultTextField
import com.dkproject.presentation.ui.component.button.DefaultButton
import com.dkproject.presentation.ui.theme.AppTheme

@Composable
fun DescriptionScreen(
    title: String,
    description: String,
    titleErrorMessage: String = "",
    descriptionErrorMessage: String = "",
    titleChange: (String) -> Unit,
    descriptionChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            Text(text = stringResource(R.string.title),
                style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${title.length}/25")
        }

        Spacer(modifier = Modifier.height(4.dp))
        DefaultTextField(
            value = title,
            onValueChange = titleChange,
            label = stringResource(R.string.title),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
        AnimatedVisibility(titleErrorMessage.isNotEmpty()) {
            Text(text = titleErrorMessage, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${description.length}/200")
        }

        Spacer(modifier = Modifier.height(4.dp))
        DefaultTextField(
            value = description,
            onValueChange = descriptionChange,
            label = "",
            maxLines = 10,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = 220.dp, max = 220.dp)
        )
        AnimatedVisibility(descriptionErrorMessage.isNotEmpty()) {
            Text(text = descriptionErrorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(32.dp))
        DefaultButton(
            title = stringResource(R.string.next),
            onClick = onConfirmClick,
            enabled = title.isNotEmpty() && description.isNotEmpty() && title.length >= 5,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DescriptionScreenPreview() {
    AppTheme {
        DescriptionScreen(
            title = "title",
            description = "asdfasfasdfasdfd\nasdfasdfasdf\naasdfadsf",
            titleChange = {},
            descriptionChange = {},
            onConfirmClick = {}
        )
    }
}