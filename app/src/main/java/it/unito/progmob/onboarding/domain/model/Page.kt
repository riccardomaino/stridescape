package it.unito.progmob.onboarding.domain.model

import androidx.annotation.DrawableRes
import it.unito.progmob.R
import it.unito.progmob.core.presentation.util.UiText

/**
 * Data class representing a single page in the onboarding flow.
 *
 * @param title The title of the page.
 *@param description The description of the page.
 * @param imageContentDescription The content description for the image on the page.
 * @param image The resource ID of the image on the page.
 */
data class Page(
    val title: UiText,
    val description: UiText,
    val imageContentDescription: UiText,
    @DrawableRes val image: Int?
)

/**
 * Returns a list of onboarding pages.
 *
 * @return A list of [Page] objects.
 */
fun getOnboardingPages(): List<Page> {
    val pages = listOf(
        Page(
            title = UiText.StringResource(resId = R.string.onboarding_page1_title),
            description = UiText.StringResource(resId = R.string.onboarding_page1_description),
            imageContentDescription = UiText.StringResource(resId = R.string.onboarding_page1_image_content_desc),
            image = R.drawable.homepage
        ),
        Page(
            title = UiText.StringResource(resId = R.string.onboarding_page2_title),
            description = UiText.StringResource(resId = R.string.onboarding_page2_description),
            imageContentDescription = UiText.StringResource(resId = R.string.onboarding_page2_image_content_desc),
            image = R.drawable.livemap
        ),
        Page(
            title = UiText.StringResource(resId = R.string.onboarding_page3_title),
            description = UiText.StringResource(resId = R.string.onboarding_page3_description),
            imageContentDescription = UiText.StringResource(resId = R.string.onboarding_page3_image_content_desc),
            image = R.drawable.history
        )
    )
    return pages
}

