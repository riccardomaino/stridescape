package it.unito.progmob.onboarding.presentation.utils

import androidx.annotation.DrawableRes
import it.unito.progmob.R

data class Page(
    val title: String,
    val description: String,
    val imageContentDescription: String = title,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = "Welcome",
        description = "This is a simple onboarding screen",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Simple and Easy",
        description = "This is a simple onboarding screen",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Get Started",
        description = "This is a simple onboarding screen",
        image = R.drawable.onboarding3
    )
)