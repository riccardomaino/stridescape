package it.unito.progmob.onboarding.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import it.unito.progmob.R
import it.unito.progmob.onboarding.domain.model.getOnboardingPages
import it.unito.progmob.onboarding.presentation.components.OnBoardingButton
import it.unito.progmob.onboarding.presentation.components.OnBoardingTextButton
import it.unito.progmob.onboarding.presentation.components.OnBoardingTop
import it.unito.progmob.onboarding.presentation.components.PageIndicator
import it.unito.progmob.ui.theme.large
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    onBoardingEvent: (OnBoardingEvent) -> Unit
) {
    Column {
        val context = LocalContext.current
        val pages = remember { mutableStateOf(getOnboardingPages(context)) }
        val pagerState = rememberPagerState(initialPage = 0) { pages.value.size }

        val buttonState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    0 -> listOf("", context.getString(R.string.onboarding_next_btn))
                    1 -> listOf(
                        context.getString(R.string.onboarding_back_btn),
                        context.getString(R.string.onboarding_next_btn)
                    )
                    2 -> listOf(
                        context.getString(R.string.onboarding_back_btn),
                        context.getString(R.string.onboarding_getstarted_btn)
                    )

                    else -> listOf("", "")
                }
            }
        }
        HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) { index ->
            OnBoardingTop(page = pages.value[index])
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = large, end = large, bottom = large)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PageIndicator(
                pagesNumber = pages.value.size,
                pagerState = pagerState
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                val coroutineScope = rememberCoroutineScope()

                if (buttonState.value[0].isNotEmpty()) {
                    OnBoardingTextButton(text = buttonState.value[0], onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                        }
                    })
                }

                OnBoardingButton(text = buttonState.value[1], onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage == 2) {
                            // TODO: Navigate to the Home screen
                            onBoardingEvent(OnBoardingEvent.SaveOnBoardingEntry)
                        } else {
                            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                        }
                    }
                })
            }
        }
    }
}