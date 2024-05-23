package it.unito.progmob.onboarding.domain.model

import android.content.Context
import androidx.annotation.DrawableRes
import it.unito.progmob.R

data class Page(
    val title: String,
    val description: String,
    val imageContentDescription: String = title,
    @DrawableRes val image: Int?
)

fun getOnboardingPages(
    context: Context
): List<Page> {
    val pages = listOf(
        Page(
            title = context.getString(R.string.onboarding_page1_title),
            description = context.getString(R.string.onboarding_page1_description),
            imageContentDescription = context.getString(R.string.onboarding_page1_image_content_description),
            image = R.drawable.homepage
        ),
        Page(
            title = context.getString(R.string.onboarding_page2_title),
            description = context.getString(R.string.onboarding_page2_description),
            imageContentDescription = "",
            image = R.drawable.livemap
        ),
        Page(
            title = context.getString(R.string.onboarding_page3_title),
            description = context.getString(R.string.onboarding_page3_description),
            image = R.drawable.history
        ),
//        Page(
//            title = context.getString(R.string.onboparding_page4_title),
//            description = context.getString(R.string.onboarding_page4_description),
//            image = null
//        )
    )

    return pages
}

