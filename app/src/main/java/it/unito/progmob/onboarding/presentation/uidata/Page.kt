package it.unito.progmob.onboarding.presentation.uidata

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
        title = "Welcome to Tracker",
        description = "Track your steps, calories, and more! \n With Tracker you can track your daily activities and improve your health. \n Make sure to allow all the permissions to get the best experience.",
        image = R.drawable.homepage
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