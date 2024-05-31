package it.unito.progmob.stats.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.presentation.StatsEvent
import it.unito.progmob.ui.theme.extralargeRadius
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.largeRadius
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.smallRadius
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsFilter(
    selectedFilter: StatsType,
    statsEvent: (StatsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showDateRangePicker by remember {
        mutableStateOf(false)
    }
    var openBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = DateUtils.getInstantFromNow(7, DateUtils.DateOperation.MINUS).toEpochMilliseconds(),
        initialSelectedEndDateMillis = null,
        initialDisplayedMonthMillis = null,
        yearRange = (2023..2024),
        initialDisplayMode = DisplayMode.Picker
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = medium, start = medium, end = medium)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(medium)
    ) {
        StatsType.entries.forEach {
            ElevatedFilterChip(
                onClick = { statsEvent(StatsEvent.FilterSelected(it)) },
                selected = it == selectedFilter,
                leadingIcon = {
                    AnimatedVisibility(it == selectedFilter) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = stringResource(R.string.stats_selected_filter_icon_content_description),
                        )
                    }
                },
                label = {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }

    IconButton(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(smallRadius)
        ).fillMaxWidth(),
        onClick = {
//            openBottomSheet = !openBottomSheet
            showDateRangePicker = !showDateRangePicker
        }
    ) {
        Icon(
            Icons.Filled.CalendarMonth,
            contentDescription = "Select a date range",
            modifier = Modifier.size(large),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }

    if (showDateRangePicker) {
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(
                    onClick = { showDateRangePicker = false },
                ) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDateRangePicker = false },
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState
            )
        }
    }


    if (openBottomSheet) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { openBottomSheet = false },
            scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
            shape = RoundedCornerShape(topStart = largeRadius, topEnd = largeRadius),
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                showModeToggle = false
            )
            Button(
                modifier = Modifier
                    .padding(medium)
                    .align(Alignment.End),
                onClick = {
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            openBottomSheet = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(extralargeRadius)
            ) {
                Text(
                    text = "Apply",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }

}