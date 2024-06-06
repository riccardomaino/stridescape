package it.unito.progmob.history.presentation.components

import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.LifecycleStartEffect
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
    val infiniteTransition = rememberInfiniteTransition(label = "Infinite transition animation")
    val alphaPositionMarker by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "Alpha animation"
    )

    val lastMarkerState = rememberMarkerState()
    val lastLocationPoint = pathPoints.lastLocationPoint()
    lastLocationPoint?.let { lastMarkerState.position = LatLng(it.lat, it.lng) }


    val startLocationPoint = pathPoints.firstLocationPoint()
    val startPoint = remember(key1 = startLocationPoint) { startLocationPoint }

    val latLngList =  mutableListOf<LatLng>()

    val lifecycleOwner = LocalLifecycleOwner.current
    LifecycleStartEffect(key1 = pathPoints, lifecycleOwner = lifecycleOwner){
        Log.d("DrawPathPoints", "onStart")
        onStopOrDispose {
            Log.d("DrawPathPoints", "onStopOrDispose")
        }
    }

    var emptyPointFounded = false
    pathPoints.forEach { pathPoint ->
        if (pathPoint is PathPoint.EmptyPoint) {
            emptyPointFounded = true
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
            )
            latLngList.clear()
        } else if (pathPoint is PathPoint.LocationPoint) {
            if (emptyPointFounded) {
                Marker(
                    icon = vectorToBitmap(
                        context = LocalContext.current,
                        parameters = BitmapParameters(
                            id = R.drawable.initial_and_final_position_marker
                        )
                    ),
                    state = rememberMarkerState(position = LatLng(pathPoint.lat, pathPoint.lng)),
                    anchor = Offset(0.5f, 0.85f),
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

//    animateColor(
//        initialValue = MaterialTheme.colorScheme.primary,
//        targetValue = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
//        animationSpec = infiniteRepeatable(
//            tween(1000),
//            repeatMode = RepeatMode.Reverse
//        ), label = "Current Marker Position Color"
//    )



    startPoint?.let {
        Marker(
            icon = vectorToBitmap(
                context = LocalContext.current,
                parameters = BitmapParameters(
                    id = R.drawable.initial_and_final_position_marker
                )
            ),
            state = rememberMarkerState(position = LatLng(it.lat, it.lng)),
            anchor = Offset(0.5f, 0.85f)
        )
    }
}