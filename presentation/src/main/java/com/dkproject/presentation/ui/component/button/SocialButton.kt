package com.dkproject.presentation.ui.component.button

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.R

@SuppressLint("SupportAnnotationUsage")
interface SocialConfiguarble {
    @get:DrawableRes
    val logo: Int
    @get:StringRes
    val title: Int
    val titleColor: Color
    val backgroundColor: Color
}

data class GoogleConfig(
    override val logo: Int = R.drawable.ic_googlelogo,
    override val title: Int = R.string.signgoogle,
    override val titleColor: Color = Color.Black,
    override val backgroundColor: Color = Color.White
) : SocialConfiguarble

data class KakaoConfig(
    override val logo: Int = R.drawable.kakaologo,
    override val title: Int = R.string.signkakao,
    override val titleColor: Color = Color.Black,
    override val backgroundColor: Color = Color(0XFFFEE500)
): SocialConfiguarble

@Composable
fun SocialButton(
    modifier: Modifier = Modifier,
    socialConfiguarble: SocialConfiguarble,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(socialConfiguarble.backgroundColor)
    ) {
        Image(
            painterResource(socialConfiguarble.logo),
            contentDescription = stringResource(socialConfiguarble.title),
            modifier = Modifier.padding(start = 16.dp),
        )

        Text(
            text = stringResource(socialConfiguarble.title),
            modifier = Modifier.padding(vertical = 12.dp)
                .padding(end = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = socialConfiguarble.titleColor,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SocialButtonPreview() {
    SocialButton(modifier = Modifier.fillMaxWidth(), socialConfiguarble = KakaoConfig())
}


