package it.unito.progmob.onboarding.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import it.unito.progmob.onboarding.presentation.components.OnBoardingButton
import it.unito.progmob.onboarding.presentation.components.OnBoardingTextButton
import it.unito.progmob.onboarding.presentation.components.OnBoardingTop
import it.unito.progmob.onboarding.presentation.components.PageIndicator
import it.unito.progmob.onboarding.presentation.uidata.pages
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.pageIndicatorWidth
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    event: (OnBoardingEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(initialPage = 0) { pages.size }

        val buttonState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    0 -> listOf("", "Next")
                    1 -> listOf("Back", "Next")
                    2 -> listOf("Back", "Get Started")
                    else -> listOf("", "")
                }
            }
        }

        HorizontalPager(state = pagerState) { index ->
            OnBoardingTop(page = pages[index])
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = large)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PageIndicator(
                Modifier.width(pageIndicatorWidth),
                pagesNumber = pages.size,
                selectedPage = pagerState.currentPage
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
                            event(OnBoardingEvent.SaveOnboardingEntry)
                        } else {
                            pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                        }
                    }
                })
            }
        }
        Spacer(modifier = Modifier.weight(0.2f))
    }
}