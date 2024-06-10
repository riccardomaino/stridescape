package it.unito.progmob.history.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.history.presentation.components.HistoryButton
import it.unito.progmob.history.presentation.components.HistoryPopUp
import it.unito.progmob.history.presentation.components.WalkDate
import it.unito.progmob.history.presentation.components.WalkTile
import it.unito.progmob.ui.theme.extraLarge
import it.unito.progmob.ui.theme.extralargeRadius
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    historyEvent: (HistoryEvent) -> Unit,
    allGroupedWalks: List<AllWalksPerDate>,
    isDataLoaded: Boolean
) {
    val context = LocalContext.current
    val backgroundIconColor = MaterialTheme.colorScheme.secondary
    val iconColor = MaterialTheme.colorScheme.onSecondary
    var walkToShow by rememberSaveable { mutableStateOf<WalkWithPathPoints?>(null) }
    var showDateRangePicker by rememberSaveable { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = DateUtils.getInstantOfDateFromNow(7,DateUtils.DateOperation.MINUS).toEpochMilliseconds(),
        initialSelectedEndDateMillis = DateUtils.getInstantOfDateFromNow(0).toEpochMilliseconds(),
        initialDisplayedMonthMillis = null,
        yearRange = (2022..2100),
        initialDisplayMode = DisplayMode.Picker
    )
    val shapeForSharedElement = remember { RoundedCornerShape(16.dp) }


    SharedTransitionLayout(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ){
                HistoryButton(
                    onClick = { showDateRangePicker = true },
                    text = stringResource(R.string.history_date_range_btn),
                    icon = Icons.Outlined.CalendarMonth,
                    iconDescription = stringResource(R.string.history_calendar_icon_content_desc)
                )
            }
            ShowLoadingProgressIndicator(isLoaded = isDataLoaded)
            if(isDataLoaded){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(small),
                ) {
                    if(allGroupedWalks.isEmpty()){
                        item(
                            key = "no_walks_key"
                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize(),
//                                contentAlignment = Alignment.Center) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    // verticalArrangement = Arrangement.Center,
                                ){
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Canvas(
                                            modifier = Modifier.size(small)
                                        ) {
                                            drawCircle(
                                                color = backgroundIconColor,
                                                radius = 50.dp.toPx(),
                                            )
                                        }
                                        Icon(
                                            Icons.Default.Cached,
                                            contentDescription = "Content desc",
                                            modifier = Modifier.size(extraLarge),
                                            tint = iconColor
                                        )
                                    }
                                    Text(
                                        text = stringResource(R.string.history_no_walks_text),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                }
//                            }
                        }
                    }else{
                        items(
                            items = allGroupedWalks,
                            key = { it.date }
                        ) { walksPerDate ->
                            if (DateUtils.formattedCurrentDate() == walksPerDate.date) {
                                WalkDate(dateFormatted = stringResource(R.string.history_today_text))
                            } else {
                                WalkDate(dateFormatted = DateUtils.formatDateForHistory(date = walksPerDate.date, context = context))
                            }
                            walksPerDate.walks.forEach { walk ->
                                AnimatedVisibility(
                                    visible = walkToShow?.walkId != walk.walkId,
                                    enter = fadeIn() + scaleIn(),
                                    exit = fadeOut() + scaleOut(),
                                    modifier = Modifier.animateItem()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .sharedBounds(
                                                sharedContentState = rememberSharedContentState(key = "${walk.walkId}-bounds"),
                                                animatedVisibilityScope = this,
                                                clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
                                            )
                                    ) {
                                        WalkTile(
                                            modifier = modifier.sharedElement(
                                                state = rememberSharedContentState(key = walk.walkId),
                                                animatedVisibilityScope = this@AnimatedVisibility
                                            ),
                                            walk = walk,
                                            onClick = { walkToShow = walk }
                                        )
                                    }
                                }
                            }
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

    if (showDateRangePicker) {
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        historyEvent(
                            HistoryEvent.DateRangeSelected(
                                dateRangePickerState.selectedStartDateMillis ?: return@Button,
                                dateRangePickerState.selectedEndDateMillis ?: return@Button
                            )
                        )
                        showDateRangePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(extralargeRadius)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_ok_btn),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDateRangePicker = false }
                ) {
                    Text(
                        text = stringResource(R.string.dialog_cancel_btn),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        ) {
            DateRangePicker(
                modifier = modifier.then(
                    if (dateRangePickerState.displayMode == DisplayMode.Picker)
                        modifier.weight(1f)
                    else
                        modifier
                ),
                title = {
                    Text(
                        modifier = modifier.padding(top = medium, start = medium, end = medium),
                        text = "Select date range",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                headline = {
                    DateRangePickerDefaults.DateRangePickerHeadline(
                        selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis,
                        selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis,
                        displayMode = dateRangePickerState.displayMode,
                        modifier = modifier.padding(start = medium),
                        dateFormatter = DatePickerDefaults.dateFormatter()
                    )
                },
                state = dateRangePickerState
            )
        }
    }
}

@Composable
private fun ColumnScope.ShowLoadingProgressIndicator(
    isLoaded: Boolean
) {
    AnimatedVisibility(
        modifier = Modifier.align(Alignment.CenterHorizontally),
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


