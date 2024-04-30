package it.unito.progmob.onboarding.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import it.unito.progmob.onboarding.presentation.uidata.Page
import it.unito.progmob.onboarding.presentation.uidata.pages
import it.unito.progmob.ui.theme.extraLarge
import it.unito.progmob.ui.theme.large

@Composable
fun OnBoardingTop(
    modifier: Modifier = Modifier,
    page: Page
) {
    Column(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.6f)
                .clip(shape = RoundedCornerShape(bottomStart = extraLarge, bottomEnd = extraLarge)),
            painter = painterResource(id = page.image),
            contentDescription = page.imageContentDescription,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(large))
        Text(
            text = page.title,
            modifier = Modifier.padding(horizontal = extraLarge).align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(large))
        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = extraLarge),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingTopPreview() {
    OnBoardingTop(
        page = pages[0]
    )
}