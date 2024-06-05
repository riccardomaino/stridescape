package it.unito.progmob.core.domain

object Constants {
    // Preferences DataStore
    const val USER_PREFERENCES_DATASTORE = "user_preferences_datastore"
    const val ONBOARDING_ENTRY = "onboarding_entry"
    const val USER_WEIGHT_ENTRY = "user_weight_entry"
    const val USER_HEIGHT_ENTRY = "user_height_entry"
    const val USER_NAME_ENTRY = "user_name_entry"

    // Tracking Service
    const val LOCATION_TRACKING_INTERVAL = 5000L
    const val TRACKING_DEEP_LINK = "app://it.unito.progmob/stridescape/main/tracking"

    // Target
    const val DEFAULT_STEPS_TARGET = 5000

    // Chart Column
    const val CHART_COLUMN_THICKNESS = 16
    const val CHART_COLUMN_ROUNDNESS = 40

    // Chart Mean Horizontal Line
    const val CHART_HORIZONTAL_LINE_THICKNESS = 2
    const val CHART_HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING = 8
    const val CHART_HORIZONTAL_LINE_LABEL_VERTICAL_PADDING = 2
    const val CHART_HORIZONTAL_LINE_LABEL_MARGIN = 4

    // Chart Marker (Label + Indicator)
    const val CHART_LABEL_BACKGROUND_SHADOW_RADIUS = 4f
    const val CHART_LABEL_BACKGROUND_SHADOW_DY = 2f
    const val CHART_LABEL_HORIZONTAL_PADDING = 16
    const val CHART_LABEL_VERTICAL_PADDING = 8
    const val CHART_LABEL_MIN_WIDTH = 40
    const val CHART_INDICATOR_PADDING = 10
    const val CHART_INDICATOR_FRONT_PADDING = 5
    const val CHART_INDICATOR_SIZE = 36f
    const val CHART_CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f

}