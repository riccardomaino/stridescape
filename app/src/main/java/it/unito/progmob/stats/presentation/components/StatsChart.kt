package it.unito.progmob.stats.presentation.components

import android.graphics.Typeface
import android.text.Layout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.decoration.rememberHorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.fullWidth
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberLayeredComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.shape.markerCornered
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasureContext
import com.patrykandpatrick.vico.core.cartesian.HorizontalDimensions
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.Insets
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.Corner
import com.patrykandpatrick.vico.core.common.shape.Shape
import it.unito.progmob.R
import it.unito.progmob.core.domain.Constants.CHART_CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER
import it.unito.progmob.core.domain.Constants.CHART_COLUMN_ROUNDNESS
import it.unito.progmob.core.domain.Constants.CHART_COLUMN_THICKNESS
import it.unito.progmob.core.domain.Constants.CHART_HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING
import it.unito.progmob.core.domain.Constants.CHART_HORIZONTAL_LINE_LABEL_MARGIN
import it.unito.progmob.core.domain.Constants.CHART_HORIZONTAL_LINE_LABEL_VERTICAL_PADDING
import it.unito.progmob.core.domain.Constants.CHART_HORIZONTAL_LINE_THICKNESS
import it.unito.progmob.core.domain.Constants.CHART_INDICATOR_FRONT_PADDING
import it.unito.progmob.core.domain.Constants.CHART_INDICATOR_PADDING
import it.unito.progmob.core.domain.Constants.CHART_INDICATOR_SIZE
import it.unito.progmob.core.domain.Constants.CHART_LABEL_BACKGROUND_SHADOW_DY
import it.unito.progmob.core.domain.Constants.CHART_LABEL_BACKGROUND_SHADOW_RADIUS
import it.unito.progmob.core.domain.Constants.CHART_LABEL_HORIZONTAL_PADDING
import it.unito.progmob.core.domain.Constants.CHART_LABEL_MIN_WIDTH
import it.unito.progmob.core.domain.Constants.CHART_LABEL_VERTICAL_PADDING
import it.unito.progmob.core.domain.util.DateUtils
import it.unito.progmob.stats.domain.model.StatsType
import it.unito.progmob.stats.presentation.state.UiStatsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import java.math.RoundingMode

@Composable
fun StatsChart(
    uiStatsState: UiStatsState,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val xToDateMapExtraStoreKey = remember { ExtraStore.Key<Map<Float, LocalDate>>() }
    val meanValueExtraStoreKey = remember { ExtraStore.Key<Float>() }

    // LaunchedEffect used to update the chart data when the uiStatsState changes
    LaunchedEffect(key1 = uiStatsState) {
        withContext(Dispatchers.Default) {
            val chartData: MutableMap<LocalDate, Float> = mutableMapOf()
            var meanValue = 0f
            when (uiStatsState.statsSelected) {
                StatsType.DISTANCE -> {
                    uiStatsState.distanceChartValues.forEach {
                        chartData[it.first] = it.second
                        meanValue += it.second
                    }
                    if (meanValue > 0) {
                        meanValue =
                            (meanValue / uiStatsState.distanceChartValues.size).toBigDecimal()
                                .setScale(1, RoundingMode.HALF_UP).toFloat()
                    }
                }

                StatsType.TIME -> {
                    uiStatsState.timeChartValues.forEach {
                        chartData[it.first] = it.second.toFloat()
                        meanValue += it.second
                    }
                    if (meanValue > 0) {
                        meanValue = (meanValue / uiStatsState.timeChartValues.size).toBigDecimal()
                            .setScale(1, RoundingMode.HALF_UP).toFloat()
                    }
                }

                StatsType.CALORIES -> {
                    uiStatsState.caloriesChartValues.forEach {
                        chartData[it.first] = it.second.toFloat()
                        meanValue += it.second
                    }
                    if (meanValue > 0) {
                        meanValue =
                            (meanValue / uiStatsState.caloriesChartValues.size).toBigDecimal()
                                .setScale(1, RoundingMode.HALF_UP).toFloat()
                    }
                }

                StatsType.STEPS -> {
                    uiStatsState.stepsChartValues.forEach {
                        chartData[it.first] = it.second.toFloat()
                        meanValue += it.second
                    }
                    if (meanValue > 0) {
                        meanValue = (meanValue / uiStatsState.stepsChartValues.size).toBigDecimal()
                            .setScale(1, RoundingMode.HALF_UP).toFloat()
                    }
                }

                StatsType.SPEED -> {
                    uiStatsState.speedChartValues.forEach {
                        chartData[it.first] = it.second
                        meanValue += it.second
                    }
                    if (meanValue > 0) {
                        meanValue = (meanValue / uiStatsState.speedChartValues.size).toBigDecimal()
                            .setScale(1, RoundingMode.HALF_UP).toFloat()
                    }
                }
            }
            val xToDatesMap: Map<Float, LocalDate> =
                chartData.keys.associateBy { it.toEpochDays().toFloat() }
            modelProducer.tryRunTransaction {
                columnSeries {
                    series(x = xToDatesMap.keys, y = chartData.values)
                }
                updateExtras { it[xToDateMapExtraStoreKey] = xToDatesMap }
                updateExtras { it[meanValueExtraStoreKey] = meanValue }
            }
        }
    }

    // COLUMN Settings
    val columnColor = MaterialTheme.colorScheme.primary
    // CHART Definition
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(
                        color = columnColor,
                        thickness = CHART_COLUMN_THICKNESS.dp,
                        shape = remember { Shape.rounded(allPercent = CHART_COLUMN_ROUNDNESS) },
                    ),
                ),
            ),
            startAxis = rememberStartAxis(
                itemPlacer = AxisItemPlacer.Vertical.step({ 1f }),
                titleComponent = rememberTextComponent(
                    color = MaterialTheme.colorScheme.onSurface,
                    background = rememberShapeComponent(
                        Shape.Pill,
                        MaterialTheme.colorScheme.surface
                    ),
                    typeface = Typeface.MONOSPACE
                ),
                title = when (uiStatsState.statsSelected) {
                    StatsType.DISTANCE -> stringResource(R.string.stats_chart_y_axis_distance_title)
                    StatsType.TIME -> stringResource(R.string.stats_chart_y_axis_time_title)
                    StatsType.CALORIES -> stringResource(R.string.stats_chart_y_axis_calories_title)
                    StatsType.STEPS -> stringResource(R.string.stats_chart_y_axis_steps_title)
                    StatsType.SPEED -> stringResource(R.string.stats_chart_y_axis_speed_title)
                },
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { x, chartValues, _ ->
                    (chartValues.model.extraStore[xToDateMapExtraStoreKey][x]
                        ?: LocalDate.fromEpochDays(x.toInt()))
                        .format(DateUtils.chartFormatter)
                },
                itemPlacer = remember {
                    AxisItemPlacer.Horizontal.default(
                        spacing = 1,
                        addExtremeLabelPadding = true
                    )
                },
            ),
            decorations = listOf(rememberMeanHorizontalLine(meanValueExtraStoreKey)),
        ),
        modelProducer = modelProducer,
        modifier = modifier.fillMaxHeight(0.6f),
        marker = rememberMarker(),
        horizontalLayout = HorizontalLayout.fullWidth(),
    )
}

@Composable
private fun rememberMeanHorizontalLine(meanValueExtraStoreKey: ExtraStore.Key<Float>): HorizontalLine {
    // MEAN HORIZONTAL LINE Settings
    val lineColor = MaterialTheme.colorScheme.secondary
    val lineLabelColor = MaterialTheme.colorScheme.secondary
    val lineLabelTextColor = MaterialTheme.colorScheme.onSecondary

    // MEAN HORIZONTAL LINE Definition
    return rememberHorizontalLine(
        y = { it[meanValueExtraStoreKey] },
        line = rememberLineComponent(
            color = lineColor,
            thickness = CHART_HORIZONTAL_LINE_THICKNESS.dp
        ),
        labelComponent = rememberTextComponent(
            background = rememberShapeComponent(Shape.Pill, lineLabelColor),
            color = lineLabelTextColor,
            padding = Dimensions.of(
                CHART_HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING.dp,
                CHART_HORIZONTAL_LINE_LABEL_VERTICAL_PADDING.dp,
            ),
            margins = Dimensions.of(CHART_HORIZONTAL_LINE_LABEL_MARGIN.dp),
            typeface = Typeface.MONOSPACE,
        ),
    )
}


@Composable
internal fun rememberMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.Top,
    showIndicator: Boolean = true,
): CartesianMarker {
    // LABEL Text Settings
    val labelTextColor = MaterialTheme.colorScheme.primary

    // LABEL Background Settings
    val labelBackgroundColor = MaterialTheme.colorScheme.surface
    val labelBackgroundShape = Shape.markerCornered(Corner.FullyRounded)
    val labelBackground = rememberShapeComponent(
        shape = labelBackgroundShape,
        color = labelBackgroundColor
    ).setShadow(
        radius = CHART_LABEL_BACKGROUND_SHADOW_RADIUS,
        dy = CHART_LABEL_BACKGROUND_SHADOW_DY,
        applyElevationOverlay = true
    )

    // LABEL Definition
    val label = rememberTextComponent(
        color = labelTextColor,
        background = labelBackground,
        padding = Dimensions.of(
            CHART_LABEL_HORIZONTAL_PADDING.dp,
            CHART_LABEL_VERTICAL_PADDING.dp
        ),
        typeface = Typeface.MONOSPACE,
        textAlignment = Layout.Alignment.ALIGN_CENTER,
        minWidth = TextComponent.MinWidth.fixed(CHART_LABEL_MIN_WIDTH.dp)
    )

    // INDICATOR Settings
    val indicatorFrontComponentColor = MaterialTheme.colorScheme.surface
    val indicatorCenterComponentColor = MaterialTheme.colorScheme.primary
    val indicatorRearComponentColor = MaterialTheme.colorScheme.primary
    val indicatorFrontComponent = rememberShapeComponent(Shape.Pill, indicatorFrontComponentColor)
    val indicatorCenterComponent = rememberShapeComponent(Shape.Pill)
    val indicatorRearComponent = rememberShapeComponent(Shape.Pill)
    val indicator = rememberLayeredComponent(
        rear = indicatorRearComponent,
        front = rememberLayeredComponent(
            rear = indicatorCenterComponent,
            front = indicatorFrontComponent,
            padding = Dimensions.of(CHART_INDICATOR_FRONT_PADDING.dp),
        ),
        padding = Dimensions.of(CHART_INDICATOR_PADDING.dp)
    )
    val guideline = rememberAxisGuidelineComponent()

    // MARKER Definition
    return remember(label, labelPosition, indicator, showIndicator, guideline) {
        object : DefaultCartesianMarker(
            label = label,
            labelPosition = labelPosition,
            indicator = if (showIndicator) indicator else null,
            indicatorSizeDp = CHART_INDICATOR_SIZE,
            setIndicatorColor = if (showIndicator) {
                { _ ->
                    indicatorRearComponent.color =
                        indicatorRearComponentColor.copy(alpha = 0.15f).toArgb()
                    indicatorCenterComponent.color = indicatorCenterComponentColor.toArgb()
                    indicatorCenterComponent.setShadow(
                        radius = 12f,
                        color = indicatorCenterComponentColor.toArgb()
                    )
                }
            } else {
                null
            },
            guideline = guideline,
        ) {
            override fun getInsets(
                context: CartesianMeasureContext,
                outInsets: Insets,
                horizontalDimensions: HorizontalDimensions,
            ) {
                with(context) {
                    outInsets.top =
                        (CHART_CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * CHART_LABEL_BACKGROUND_SHADOW_RADIUS - CHART_LABEL_BACKGROUND_SHADOW_DY).pixels
                    if (labelPosition == LabelPosition.AroundPoint) {
                        return
                    }
                    outInsets.top += label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels
                }
            }
        }
    }
}