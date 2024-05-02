package it.unito.progmob.onboarding.domain.model

import android.content.Context
import androidx.annotation.DrawableRes
import it.unito.progmob.R

data class Page(
    val title: String,
    val description: String,
    val imageContentDescription: String = title,
    @DrawableRes val image: Int
)

fun getOnboardingPages(
    context: Context
): List<Page> {
    val pages = listOf(
        Page(
            title = context.getString(R.string.onboarding_page1_title),
            description = context.getString(R.string.onboarding_page1_description),
            image = R.drawable.homepage
        ),
        Page(
            title = context.getString(R.string.onboarding_page2_title),
            description = context.getString(R.string.onboarding_page2_description),
            image = R.drawable.livemap
        ),
        Page(
            title = "Get Started",
            description = "This is a simple onboarding screen",
            image = R.drawable.onboarding3
        )
    )

    return pages
}

