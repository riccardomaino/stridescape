package it.unito.progmob.core.domain.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


/**
 * Customizable set of parameters for generating a [BitmapDescriptor] for use a a map marker.
 */
data class BitmapParameters(
    @DrawableRes val id: Int,
    @ColorInt val tint: Int? = null,
//    @ColorInt val backgroundColor: Int? = null,
//    val backgroundAlpha: Int = 168,
    val padding: Int = 16,
)

// Cache of [BitmapDescriptor]s
private val bitmapCache = mutableMapOf<BitmapParameters, BitmapDescriptor>()

/**
 * Returns a [BitmapDescriptor] for a given [BitmapParameters] set.
 */
fun vectorToBitmap(context: Context, parameters: BitmapParameters): BitmapDescriptor {
    return bitmapCache[parameters] ?: createBitmapDescriptor(context, parameters)
}

/**
 * Creates a customized [BitmapDescriptor] for use as a map marker.
 */
private fun createBitmapDescriptor(
    context: Context,
    parameters: BitmapParameters
): BitmapDescriptor {
    val vectorDrawable = ResourcesCompat.getDrawable(
        context.resources,
        parameters.id,
        null
    ) ?: return run {
        BitmapDescriptorFactory.defaultMarker()
    }

    vectorDrawable.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )

    // val padding = if (parameters.backgroundColor != null) parameters.padding else 0
    // val halfPadding = padding / 2

    parameters.tint?.let {
        vectorDrawable.setTint(it)
    }

    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)

    // DrawableCompat.setTint(vectorDrawable, parameters.iconColor)

//    if (parameters.backgroundColor != null) {
//        val fillPaint = Paint().apply {
//            style = Paint.Style.FILL
//            color = parameters.backgroundColor
//            alpha = parameters.backgroundAlpha
//        }
//
//        val strokePaint = Paint().apply {
//            style = Paint.Style.STROKE
//            color = parameters.backgroundColor
//            strokeWidth = 3f
//        }
//
//        canvas.drawCircle(
//            canvas.width / 2.0f,
//            canvas.height / 2.0f,
//            canvas.width.toFloat() / 2,
//            fillPaint
//        )
//        canvas.drawCircle(
//            canvas.width / 2.0f,
//            canvas.height / 2.0f,
//            (canvas.width.toFloat() / 2) - 3,
//            strokePaint
//        )
//    }
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap).also { bitmapCache[parameters] = it }
}