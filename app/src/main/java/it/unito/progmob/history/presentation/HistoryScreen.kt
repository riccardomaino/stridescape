package it.unito.progmob.history.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.unito.progmob.R
import it.unito.progmob.core.domain.ext.bottomFadingEdge
import it.unito.progmob.core.domain.ext.fullMonthsNames
import it.unito.progmob.core.domain.ext.shortMonthsNames
import it.unito.progmob.core.domain.ext.shortWeekDaysNames
import it.unito.progmob.core.domain.ext.topFadingEdge
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.history.domain.model.AllWalksPerDate
import it.unito.progmob.history.domain.model.WalkWithPathPoints
import it.unito.progmob.history.presentation.components.HistoryButton
import it.unito.progmob.history.presentation.components.HistoryPopUp
import it.unito.progmob.history.presentation.components.WalkDate
import it.unito.progmob.history.presentation.components.WalkTile
import it.unito.progmob.tracking.presentation.components.SelectedDateText
import it.unito.progmob.ui.theme.extralargeRadius
import it.unito.progmob.ui.theme.large
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
    val shortWeekDaysNames = rememberSaveable { context.shortWeekDaysNames }
    val shortMonthsNames = rememberSaveable { context.shortMonthsNames }
    val fullMonthsNames = rememberSaveable { context.fullMonthsNames }

    var walkToShow by remember { mutableStateOf<WalkWithPathPoints?>(null) }
    var showDateRangePicker by rememberSaveable { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = DateUtils.getInstantOfDateFromNow(
            7,
            DateUtils.DateOperation.MINUS
        ).toEpochMilliseconds(),
        initialSelectedEndDateMillis = DateUtils.getInstantOfDateFromNow(0).toEpochMilliseconds(),
        initialDisplayedMonthMillis = null,
        yearRange = (2022..2100),
        initialDisplayMode = DisplayMode.Picker
    )
    val shapeForSharedElement = remember { RoundedCornerShape(16.dp) }
    val startDateComponents by remember {
        derivedStateOf {
            DateUtils.extractDateComponentsFromEpochMillis(
                epochMillis = dateRangePickerState.selectedStartDateMillis,
                monthsNames = shortMonthsNames
            )
        }
    }
    val endDateComponents by remember {
        derivedStateOf {
            DateUtils.extractDateComponentsFromEpochMillis(
                epochMillis = dateRangePickerState.selectedEndDateMillis,
                monthsNames = shortMonthsNames
            )
        }
    }


    SharedTransitionLayout(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HistoryButton(
                onClick = { showDateRangePicker = true },
                text = stringResource(R.string.history_date_range_btn),
                icon = Icons.Outlined.CalendarMonth,
                iconDescription = stringResource(R.string.history_calendar_icon_content_desc)
            )
            SelectedDateText(
                startDate = startDateComponents,
                endDate = endDateComponents
            )
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                ShowLoadingProgressIndicator(isLoaded = isDataLoaded)
                if (isDataLoaded) {
                    if (allGroupedWalks.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = large),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.empty_search),
                                contentDescription = stringResource(R.string.history_empty_search_image_content_desc)
                            )
                            Text(
                                text = stringResource(R.string.history_no_walks_title),
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.history_no_walks_description),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .topFadingEdge(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    width = medium,
                                    isVisible = true
                                )
                                .bottomFadingEdge(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    width = medium,
                                    isVisible = true
                                )
                                .padding(small),
                        ) {
                            items(
                                items = allGroupedWalks,
                                key = { it.date }
                            ) { walksPerDate ->
                                if (DateUtils.formattedCurrentDate() == walksPerDate.date) {
                                    WalkDate(dateFormatted = stringResource(R.string.history_today_text))
                                } else {
                                    WalkDate(
                                        dateFormatted = DateUtils.formatDateFromStringExpanded(
                                            date = walksPerDate.date,
                                            weekDaysNames = shortWeekDaysNames,
                                            monthsNames = fullMonthsNames
                                        )
                                    )
                                }
                                walksPerDate.walks.forEach { walk ->
                                    this@Column.AnimatedVisibility(
                                        visible = walkToShow?.walkId != walk.walkId,
                                        enter = fadeIn() + scaleIn(),
                                        exit = fadeOut() + scaleOut(),
                                        modifier = Modifier.animateItem()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .sharedBounds(
                                                    sharedContentState = rememberSharedContentState(
                                                        key = "${walk.walkId}-bounds"
                                                    ),
                                                    animatedVisibilityScope = this,
                                                    clipInOverlayDuringTransition = OverlayClip(
                                                        shapeForSharedElement
                                                    )
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
        }

        HistoryPopUp(
            modifier = modifier,
            walkToShow = walkToShow,
            onBackBehaviour = { walkToShow = null },
            onClick = { walkToShow = null }
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
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .wrapContentSize()
        )
    }
}


