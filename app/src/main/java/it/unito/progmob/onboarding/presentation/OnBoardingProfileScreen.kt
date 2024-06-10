package it.unito.progmob.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import it.unito.progmob.R
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.onboarding.presentation.components.OnBoardingButton
import it.unito.progmob.onboarding.presentation.components.OnBoardingProfileTop
import it.unito.progmob.onboarding.presentation.components.OnBoardingTextButton
import it.unito.progmob.onboarding.presentation.state.UiOnBoardingState
import it.unito.progmob.ui.theme.large
import kotlinx.coroutines.launch

@Composable
fun OnBoardingProfileScreen(
    modifier: Modifier = Modifier,
    onBoardingEvent: (OnBoardingEvent) -> Unit,
    onBoardingState: UiOnBoardingState,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        OnBoardingProfileTop(
            onBoardingEvent = onBoardingEvent,
            onBoardingState = onBoardingState
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .padding(start = large, end = large, bottom = large)
                .navigationBarsPadding()
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            OnBoardingTextButton(
                text = stringResource(R.string.onboarding_back_btn),
                onClick = {
                    coroutineScope.launch {
                        navController.navigate(route = Route.OnBoardingScreenRoute.route)
                    }
                })
            OnBoardingButton(
                text = stringResource(R.string.onboarding_getstarted_btn),
                onClick = {
                    coroutineScope.launch {
                        navController.navigate(route = Route.HomeScreenRoute.route) {
                            popUpTo(Route.HomeScreenRoute.route) { inclusive = true }
                        }
                    }
                    onBoardingEvent(OnBoardingEvent.SaveProfile)
                },
                isEnabled = onBoardingState.usernameError == null &&
                        onBoardingState.heightError == null &&
                        onBoardingState.weightError == null &&
                        onBoardingState.targetError == null &&
                        onBoardingState.username.isNotBlank() &&
                        onBoardingState.height.isNotBlank() &&
                        onBoardingState.weight.isNotBlank() &&
                        onBoardingState.target.isNotBlank()
            )
        }
    }
}