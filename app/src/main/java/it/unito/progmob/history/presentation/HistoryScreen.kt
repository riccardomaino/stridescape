package it.unito.progmob.history.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.history.presentation.components.HistoryPopUp
import it.unito.progmob.history.presentation.components.SingleWalkStat
import it.unito.progmob.history.presentation.components.WalkDate
import it.unito.progmob.ui.theme.small

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    allWalksList: List<AllWalksPerDate>,
    isDataLoaded: Boolean
) {
    val scrollState = rememberLazyListState()
    val showPopUp = remember { mutableStateOf(false) }
    var walkToShow by remember { mutableStateOf<WalkWithPathPoints?>(null) }
    val prepareData by remember { mutableStateOf(false) }
    val shapeForSharedElement = RoundedCornerShape(16.dp)

    SharedTransitionLayout(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(small),
        ) {
            items(
                items = allWalksList,
                key = { it.date }
            ) { allWalksPerDate ->
                if (DateUtils.formattedCurrentDate(formatter = DateUtils.defaultFormatter) == allWalksPerDate.date) {
                    WalkDate(date = stringResource(R.string.history_today_text))
                } else {
                    WalkDate(date = allWalksPerDate.date)
                }
                allWalksPerDate.walks.forEach { walkWithPathPoints ->
                    AnimatedVisibility(
                        visible = walkToShow?.walkId != walkWithPathPoints.walkId,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier.animateItem()
                    ) {
                        Box(
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "${walkWithPathPoints.walkId}-bounds"),
                                    // Using the scope provided by AnimatedVisibility
                                    animatedVisibilityScope = this,
                                    clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
                                )
                        ) {
                            SingleWalkStat(
                                modifier = modifier.sharedElement(
                                    state = rememberSharedContentState(key = walkWithPathPoints.walkId),
                                    animatedVisibilityScope = this@AnimatedVisibility
                                ),
                                singleWalk = walkWithPathPoints,
                                showPopUp = showPopUp.value,
                                onClick = {
                                    walkToShow = walkWithPathPoints
//                                        showPopUp.value = !showPopUp.value
                                }
                            )
                        }
                    }
                }
            }
        }
        HistoryPopUp(
            modifier = modifier,
            walkToShow = walkToShow,
            backHandler = {
                walkToShow = null
            },
            onClick = {
                walkToShow = null
            }
        )
    }
}

//        AnimatedVisibility(
//            visible = showPopUp.value,
//            enter = fadeIn(
//                animationSpec = tween(durationMillis = 150, delayMillis = 0)
//            ) + expandIn(
//                animationSpec = tween(durationMillis = 200, delayMillis = 50),
//                expandFrom = Alignment.Center,
//                initialSize = { it / 2 }
//            ),
//            exit = fadeOut(
//                animationSpec = tween(durationMillis = 150, delayMillis = 50)
//            ) + shrinkOut(
//                animationSpec = tween(durationMillis = 200, delayMillis = 0),
//                targetSize = { it / 2 },
//                shrinkTowards = Alignment.Center
//            )
//        ) {
//
//        }


@Composable
private fun BoxScope.ShowLoadingProgressIndicator(
    isLoaded: Boolean
) {
    AnimatedVisibility(
        modifier = Modifier
            .matchParentSize(),
        visible = !isLoaded,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        CircularProgressIndicator(
            strokeWidth = 10.dp,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .wrapContentSize()
        )
    }
}


