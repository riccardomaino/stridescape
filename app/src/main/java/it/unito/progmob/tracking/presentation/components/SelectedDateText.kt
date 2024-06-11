package it.unito.progmob.tracking.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import it.unito.progmob.R

@Composable
fun SelectedDateText(
    startDate: Array<String>?,
    endDate: Array<String>?,
) {
    val annotatedString = buildAnnotatedString {
        startDate?.let {
            val monthStr = it[0]
            val dayStr = it[1]
            val yearStr = it[2]
            withStyle(style = spanStyleBold.copy(color = MaterialTheme.colorScheme.primary)){
                append(monthStr)
            }
            withStyle(style = spanStyleExtraLight.copy(color = MaterialTheme.colorScheme.primary)){
                append(", $dayStr ")
                append(yearStr)
            }
        } ?: run {
            withStyle(
                style = spanStyleBold.copy(color = MaterialTheme.colorScheme.primary)
            ){
                append(stringResource(R.string.history_start_date_text_first))
            }
            withStyle(
                style = spanStyleExtraLight.copy(color = MaterialTheme.colorScheme.primary)
            ){
                append(" ")
                append(stringResource(R.string.history_start_date_text_second))
            }
        }

        withStyle(
            style = spanStyleNormal.copy(color = MaterialTheme.colorScheme.primary)
        ){
            append(" - ")
        }

        endDate?.let {
            val monthStr = it[0]
            val dayStr = it[1]
            val yearStr = it[2]
            withStyle(style = spanStyleBold.copy(color = MaterialTheme.colorScheme.primary)){
                append(monthStr)
            }
            withStyle(style = spanStyleExtraLight.copy(color = MaterialTheme.colorScheme.primary)){
                append(", $dayStr ")
                append(yearStr)
            }
        } ?: run {
            withStyle(
                style = spanStyleBold.copy(color = MaterialTheme.colorScheme.primary)
            ){
                append(stringResource(R.string.history_end_date_text_first))
            }
            withStyle(
                style = spanStyleExtraLight.copy(color = MaterialTheme.colorScheme.primary)
            ){
                append(" ")
                append(stringResource(R.string.history_end_date_text_second))
            }
        }
    }
    Text(text = annotatedString)
}

private val spanStyleNormal = SpanStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp
)

private val spanStyleExtraLight = SpanStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.ExtraLight,
    fontSize = 22.sp
)

private val spanStyleBold = SpanStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp
)
