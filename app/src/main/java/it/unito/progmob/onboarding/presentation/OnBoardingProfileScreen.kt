package it.unito.progmob.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import it.unito.progmob.R
import it.unito.progmob.core.presentation.navigation.Route
import it.unito.progmob.onboarding.presentation.components.OnBoardingButton
import it.unito.progmob.onboarding.presentation.components.OnBoardingTextButton
import it.unito.progmob.onboarding.presentation.components.OnBoardingTextField
import it.unito.progmob.ui.theme.extraLarge
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small
import kotlinx.coroutines.launch

@Composable
fun OnBoardingProfileScreen(
    modifier: Modifier = Modifier,
    onBoardingEvent: (OnBoardingEvent) -> Unit,
    navController: NavController,
    userWeight: MutableState<String>,
    userHeight: MutableState<String>,
    userName: MutableState<String>
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = medium), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = modifier.height(medium))
                Text(
                    text = stringResource(R.string.onboparding_page4_title),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
                Spacer(modifier = modifier.height(extraLarge))
            }
        },
        bottomBar = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = large, vertical = large),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
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
                            navController.navigate(route = Route.OnBoardingProfileScreenRoute.route) {
                                popUpTo(Route.OnBoardingScreenRoute.route) { inclusive = true }
                            }
                        }
                        onBoardingEvent(OnBoardingEvent.SaveEntries)
                    })
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .shadow(small, shape = RoundedCornerShape(large))
                    .clip(RoundedCornerShape(large))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top =  extraLarge, start = extraLarge, end = extraLarge, bottom = extraLarge)
                    .fillMaxWidth(0.8f)
            ) {
                Text(text = stringResource(R.string.onboarding_page4_description), style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center))
                Spacer(modifier = modifier.height(medium))
                OnBoardingTextField(
                    textFieldTitle = "Name",
                    value = userName.value,
                    suffix = "",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = { userName.value = it },
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = modifier.height(extraLarge))
                OnBoardingTextField(
                    textFieldTitle = "Height",
                    value = userHeight.value,
                    suffix = "cm",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { userHeight.value = it },
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = modifier.height(extraLarge))
                OnBoardingTextField(
                    textFieldTitle = "Weight",
                    value = userWeight.value,
                    suffix = "kg",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { userWeight.value = it },
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}