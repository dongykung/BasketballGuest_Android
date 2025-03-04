package com.dkproject.presentation.ui.component.Image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.dkproject.presentation.R

@Composable
fun CustomImage(
    modifier: Modifier =Modifier,
    url:String,
) {
    Box(modifier = modifier) {
        Image(
            painter = rememberAsyncImagePainter(
                model = url,
                placeholder = painterResource(id = R.drawable.placeholderimage),
                error = painterResource(id = R.drawable.placeholderimage)
            ), contentDescription = "Profile",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )

    }
}