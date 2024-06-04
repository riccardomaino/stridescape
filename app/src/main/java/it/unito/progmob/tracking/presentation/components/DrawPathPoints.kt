package it.unito.progmob.tracking.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState
import it.unito.progmob.core.domain.util.WalkUtils.firstLocationPoint
import it.unito.progmob.core.domain.util.WalkUtils.lastLocationPoint
import it.unito.progmob.tracking.domain.model.PathPoint

@Composable
@GoogleMapComposable
fun DrawPathPoints(
    pathPoints: List<PathPoint>,
    isRunningFinished: Boolean,
) {
    val lastMarkerState = rememberMarkerState()
    val lastLocationPoint = pathPoints.lastLocationPoint()
    lastLocationPoint?.let { lastMarkerState.position = LatLng(it.lat, it.lng) }

    val firstLocationPoint = pathPoints.firstLocationPoint()
    val firstPoint = remember(key1 = firstLocationPoint) { firstLocationPoint }

    val latLngList = mutableListOf<LatLng>()

    pathPoints.forEach { pathPoint ->
        if (pathPoint is PathPoint.EmptyPoint) {
            Polyline(
                points = latLngList.toList(),
                color = MaterialTheme.colorScheme.primary,
            )
            latLngList.clear()
        } else if (pathPoint is PathPoint.LocationPoint) {
            latLngList += LatLng(pathPoint.lat, pathPoint.lng)
        }
    }


    //add the last path points
    if (latLngList.isNotEmpty()) {
        Polyline(
            points = latLngList.toList(),
            color = MaterialTheme.colorScheme.primary,
            width = 10f
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val lastMarkerPointColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        animationSpec = infiniteRepeatable(
            tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "marker position color"
    )

    Marker(
        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
//        icon = bitmapDescriptorFromVector(
//            context = LocalContext.current,
//            vectorResId = R.drawable.ic_circle,
//            tint = (if (isRunningFinished) md_theme_light_primary else lastMarkerPointColor).toArgb()
//        ),
        state = lastMarkerState,
        anchor = Offset(0.5f, 0.5f),
        visible = lastLocationPoint != null
    )

    firstPoint?.let {
        Marker(
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
//            icon = bitmapDescriptorFromVector(
//                context = LocalContext.current,
//                vectorResId = if (isRunningFinished) R.drawable.ic_circle else R.drawable.ic_circle_hollow,
//                tint = if (isRunningFinished) md_theme_light_primary.toArgb() else null
//            ),
            state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
            anchor = Offset(0.5f, 0.5f)
        )
    }
}