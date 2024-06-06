package it.unito.progmob.history.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.history.presentation.components.HistoryPopUp
import it.unito.progmob.history.presentation.components.SingleWalkStat
import it.unito.progmob.history.presentation.components.WalkDate
import it.unito.progmob.ui.theme.small

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    allWalks: List<AllWalksPerDate>
) {
    val scrollState = rememberLazyListState()
    val showPopUp = remember { mutableStateOf(false) }
    var walkToShow by remember { mutableStateOf<WalkWithPathPoints?>(null) }

//    DisposableEffect(key1 = true) {
//        Log.d("HistoryScreen", "DisposableEffect")
//        val onBackInvokedCallback = {showPopUp = false}
//        mainActivity.onBackInvokedDispatcher.registerOnBackInvokedCallback(PRIORITY_DEFAULT, onBackInvokedCallback)
//        onDispose {
//            Log.d("HistoryScreen", "onDispose")
//            mainActivity.onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInvokedCallback)
//        }
//    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(small),
            state = scrollState,
        ) {
            items(allWalks) { walk ->
                if (DateUtils.formattedCurrentDate(formatter = DateUtils.defaultFormatter) == walk.date) {
                    WalkDate(date = "Today")
                } else {
                    WalkDate(date = walk.date)
                }
                walk.walks.forEach { walkWithPathPoints ->
                    SingleWalkStat(singleWalk = walkWithPathPoints, onClick = {
                        walkToShow = walkWithPathPoints
                        showPopUp.value = !showPopUp.value
                    })
                }
            }
        }
        AnimatedVisibility(
            visible = showPopUp.value, enter = fadeIn(
                animationSpec = tween(durationMillis = 150)
            ), exit =
            fadeOut(
                animationSpec = tween(durationMillis = 150, delayMillis = 100)
            )
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    .clickable(onClick = { showPopUp.value = !showPopUp.value }),
                contentAlignment = Alignment.Center,
            ) {}
        }
        AnimatedVisibility(
            visible = showPopUp.value, enter = fadeIn(
                animationSpec = tween(durationMillis = 150, delayMillis = 100)
            ) + expandIn(
                animationSpec = tween(durationMillis = 150, delayMillis = 100),
                expandFrom = Alignment.Center
            )
        ) {
            HistoryPopUp(
                modifier = modifier,
                walkToShow = walkToShow!!,
                showPopUp = showPopUp
            )
        }


    }
}