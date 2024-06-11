package it.unito.progmob.tracking.presentation.components

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState
import it.unito.progmob.R
import it.unito.progmob.core.domain.util.BitmapParameters
import it.unito.progmob.core.domain.util.WalkUtils.firstLocationPoint
import it.unito.progmob.core.domain.util.WalkUtils.lastLocationPoint
import it.unito.progmob.core.domain.util.vectorToBitmap
import it.unito.progmob.tracking.domain.model.PathPoint

@Composable
@GoogleMapComposable
fun DrawPathPoints(
    pathPoints: List<PathPoint>,
    isTracking: Boolean,
) {
//    val infiniteTransition = rememberInfiniteTransition(label = "Infinite transition animation")
//    val alphaPositionMarker by infiniteTransition.animateFloat(
//        initialValue = 0.5f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            tween(1000),
//            repeatMode = RepeatMode.Reverse
//        ), label = "Alpha animation"
//    )

    // Current Position handler variables
    val currentPositionMarkerState = rememberMarkerState()
    val lastLocationPoint = pathPoints.lastLocationPoint()
    lastLocationPoint?.let { currentPositionMarkerState.position = LatLng(it.lat, it.lng) }

    // Start Point handler variables
    var startPointDrawn = rememberSaveable { false }
    val startLocationPoint = pathPoints.firstLocationPoint()
    val startPoint = remember(key1 = startLocationPoint) { startLocationPoint }

    // LatLng list used to draw the path points
    val latLngList =  mutableListOf<LatLng>()

//    val startEndMarkerList = remember { mutableListOf<MarkerState>() }
//
//    LaunchedEffect(key1 = pathPoints) {
//        pathPoints.lastEmptyPointIndex().also  { lastEmptyPointIndex ->
//            if (lastEmptyPointIndex == -1) return@LaunchedEffect
//
//            val prevLocationPoint = pathPoints[lastEmptyPointIndex - 1] as PathPoint.LocationPoint
//            startEndMarkerList.add(MarkerState(position = LatLng(prevLocationPoint.lat, prevLocationPoint.lng)))
//        }
//    }

    var emptyPointFounded = false
    pathPoints.forEachIndexed { index, pathPoint ->
        if (pathPoint is PathPoint.EmptyPoint) {
            emptyPointFounded = true
            val prevLocationPoint = pathPoints[index - 1] as PathPoint.LocationPoint
            Marker(
                title = stringResource(R.string.pathpoint_marker_title_end_point),
                icon = vectorToBitmap(
                    context = LocalContext.current,
                    parameters = BitmapParameters(
                        id = R.drawable.initial_and_final_position_marker
                    )
                ),
                state = MarkerState(position = LatLng(prevLocationPoint.lat, prevLocationPoint.lng)),
                anchor = Offset(0.5f, 0.95f),
                visible = true
            )
            Polyline(
                points = latLngList.toList(),
                color = MaterialTheme.colorScheme.primary,
            )
            latLngList.clear()
        } else if (pathPoint is PathPoint.LocationPoint) {
            if (emptyPointFounded) {
                Marker(
                    title = stringResource(R.string.pathpoint_marker_title_start_point),
                    icon = vectorToBitmap(
                        context = LocalContext.current,
                        parameters = BitmapParameters(
                            id = R.drawable.initial_and_final_position_marker
                        )
                    ),
                    state = MarkerState(position = LatLng(pathPoint.lat, pathPoint.lng)),
                    anchor = Offset(0.5f, 0.95f),
                    visible = true
                )
                emptyPointFounded = false
            }
            latLngList += LatLng(pathPoint.lat, pathPoint.lng)
        }
    }

    // Add the last path points
    if (latLngList.isNotEmpty()) {
        Polyline(
            points = latLngList.toList(),
            color = MaterialTheme.colorScheme.primary,
            width = 10f
        )
    }

    Marker(
        icon = vectorToBitmap(
            context = LocalContext.current,
            parameters = BitmapParameters(
                id = R.drawable.current_position_marker_1
            )
        ),
        state = currentPositionMarkerState,
        anchor = Offset(0.5f, 0.5f),
        visible = isTracking
    )

    if(!startPointDrawn){
        startPoint?.let {
            Log.d("DrawPathPoints", "START POINT Drawn")
            startPointDrawn = true
            Marker(
                title = stringResource(R.string.pathpoint_marker_title_start_point),
                icon = vectorToBitmap(
                    context = LocalContext.current,
                    parameters = BitmapParameters(
                        id = R.drawable.initial_and_final_position_marker
                    )
                ),
                state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
                anchor = Offset(0.5f, 0.95f)
            )
        }
    }
}