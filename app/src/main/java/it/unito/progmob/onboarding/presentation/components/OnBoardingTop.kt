package it.unito.progmob.onboarding.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import it.unito.progmob.onboarding.domain.model.Page
import it.unito.progmob.onboarding.domain.model.getOnboardingPages
import it.unito.progmob.ui.theme.extraLarge
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium

@Composable
fun OnBoardingTop(
    modifier: Modifier = Modifier,
    page: Page
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(start = large, end = large, top = medium)
                .fillMaxHeight(fraction = 0.6f),
            contentAlignment = Alignment.BottomCenter
        ) {
            page.image?.let { painterResource(id = it) }?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(topStart = large, topEnd = large)),
                    painter = it,
                    contentDescription = page.imageContentDescription.asString(),
                    contentScale = ContentScale.Crop,
                    )
            }
        }
        Spacer(modifier = Modifier.height(large))
        Text(
            text = page.title.asString(),
            modifier = Modifier
                .padding(horizontal = extraLarge),
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(large))
        Text(
            text = page.description.asString(),
            modifier = Modifier.padding(horizontal = extraLarge),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingTopPreview() {
    val pages = getOnboardingPages()
    OnBoardingTop(
        page = pages[0]
    )
}