package it.unito.progmob.stats.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.presentation.StatsEvent
import it.unito.progmob.ui.theme.extralargeRadius
import it.unito.progmob.ui.theme.large
import it.unito.progmob.ui.theme.medium
import it.unito.progmob.ui.theme.small
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsFilter(
    selectedFilter: StatsType,
    statsEvent: (StatsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var showDateRangePicker by remember {
        mutableStateOf(false)
    }
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = DateUtils.getInstantOfDateFromNow(
            7,
            DateUtils.DateOperation.MINUS
        ).toEpochMilliseconds(),
        initialSelectedEndDateMillis = DateUtils.getInstantOfDateFromNow(0).toEpochMilliseconds(),
        initialDisplayedMonthMillis = null,
        yearRange = (2024..2100),
        initialDisplayMode = DisplayMode.Picker
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = small, start = small, end = small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
//                .fillMaxWidth(0.85f)
                .weight(3f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatsType.entries.forEach {
                ElevatedFilterChip(
                    onClick = { statsEvent(StatsEvent.StatsTypeSelected(it)) },
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
        Spacer(modifier = modifier.width(small))
        IconButton(
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(extralargeRadius)
                )
                .weight(1f),
            onClick = {
                showDateRangePicker = true
            }
        ) {
            Icon(
                Icons.Filled.CalendarMonth,
                contentDescription = stringResource(R.string.stats_button_icon_content_description),
                modifier = Modifier.size(large),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

    }



    if (showDateRangePicker) {
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            statsEvent(
                                StatsEvent.DateRangeSelected(
                                    dateRangePickerState.selectedStartDateMillis ?: return@launch,
                                    dateRangePickerState.selectedEndDateMillis ?: return@launch
                                )
                            )
                        }
                        showDateRangePicker = false
                    },
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