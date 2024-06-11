package it.unito.progmob.history.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
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
fun DrawHistoryPathPoints(
    pathPoints: List<PathPoint>,
) {
    val lastMarkerState = rememberMarkerState()
    val lastLocationPoint = pathPoints.lastLocationPoint()
    lastLocationPoint?.let { lastMarkerState.position = LatLng(it.lat, it.lng) }

    val startLocationPoint = pathPoints.firstLocationPoint()
    val startPoint = remember(key1 = startLocationPoint) { startLocationPoint }

    val latLngList = mutableListOf<LatLng>()

    var emptyPointFounded = false
    pathPoints.forEachIndexed { index, pathPoint  ->
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
                state = rememberMarkerState(position = LatLng(prevLocationPoint.lat, prevLocationPoint.lng)),
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
                    title =  stringResource(R.string.pathpoint_marker_title_start_point),
                    icon = vectorToBitmap(
                        context = LocalContext.current,
                        parameters = BitmapParameters(
                            id = R.drawable.initial_and_final_position_marker
                        )
                    ),
                    state = rememberMarkerState(position = LatLng(pathPoint.lat, pathPoint.lng)),
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
        lastLocationPoint?.let {
            Marker(
                icon = vectorToBitmap(
                    context = LocalContext.current,
                    parameters = BitmapParameters(
                        id = R.drawable.initial_and_final_position_marker
                    )
                ),
                state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
                anchor = Offset(0.5f, 0.85f),
                visible = true
            )
        }
        Polyline(
            points = latLngList.toList(),
            color = MaterialTheme.colorScheme.primary,
            width = 10f
        )
    }

    startPoint?.let {
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