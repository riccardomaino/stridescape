package it.unito.progmob.history.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import it.unito.progmob.core.domain.model.WalkWithPathPoints
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.history.presentation.components.SingleWalkStat
import it.unito.progmob.history.presentation.components.WalkDate
import it.unito.progmob.ui.theme.small

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    allWalks:  List<WalkWithPathPoints>
) {
    val scrollState = rememberLazyListState()
    val allWalksIterator = allWalks.listIterator()
    var prevWalk: WalkWithPathPoints? = remember { null }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(small),
        state = scrollState,
    ) {
        items(allWalks) { walk ->
            Log.d("HistoryScreen", "walk: ${walk.walk.date}, prevWalk: ${prevWalk?.walk?.date}")
            prevWalk?.let {
                if (it.walk.date != walk.walk.date) {
                    WalkDate(modifier, walk.walk.date)
                }
                SingleWalkStat(singleWalk = walk.walk)
            } ?: run {
                if (DateUtils.formattedCurrentDate(formatter = DateUtils.defaultFormatter) == walk.walk.date) {
                    WalkDate(modifier, "Today")
                    SingleWalkStat(singleWalk = walk.walk)
                } else {
                    WalkDate(modifier, walk.walk.date)
                    SingleWalkStat(singleWalk = walk.walk)
                }
            }
            prevWalk = walk
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}