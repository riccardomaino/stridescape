package it.unito.progmob.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import it.unito.progmob.R


val nunitoFamily = FontFamily(
    Font(R.font.nunito_light, FontWeight.Light),
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold),
    Font(R.font.nunito_italic, FontWeight.Normal, FontStyle.Italic),
    
)

// Set of Material typography styles to start with

val defaultTypography = Typography()

val Typography = Typography(

    displayMedium = defaultTypography.displayMedium.copy(fontFamily = nunitoFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = nunitoFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = nunitoFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = nunitoFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = nunitoFamily),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = nunitoFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = nunitoFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = nunitoFamily),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = nunitoFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = nunitoFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = nunitoFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = nunitoFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = nunitoFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = nunitoFamily)
)


    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */