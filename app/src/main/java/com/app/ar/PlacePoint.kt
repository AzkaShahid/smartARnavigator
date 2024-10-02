package com.app.ar

import android.content.Context
import android.graphics.*
import android.location.Location
import android.opengl.Matrix
import androidx.core.content.res.ResourcesCompat
import com.app.R
import kotlin.math.roundToInt


class PlacePoint constructor(
    val place: Place,
    val enu: ENUCoordinate,
    private val x: Float,
    private val y: Float,
    private val z: Float,
    val locationModel: LocationModel
) {
    companion object {

        const val CIRCLE_SIZE = 120

        private val defaultPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.HSVToColor(0, floatArrayOf(0f, 0f, 0f))
        }

        private val textToShowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.WHITE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textSize = 53f
            textAlign = Paint.Align.CENTER
        }

        fun fromProjectionOfPlaceEnu(
            projectionMatrix: ProjectionMatrix,
            placeENU: PlaceENU,
            locationModel: LocationModel
        ): PlacePoint {
            val projectedPoint = floatArrayOf(
                placeENU.enu.x.toFloat(),
                placeENU.enu.y.toFloat(),
                placeENU.enu.z.toFloat(),
                1f
            )

            Matrix.multiplyMV(
                projectedPoint, 0,
                projectionMatrix.toArray(), 0,
                projectedPoint, 0
            )

            return PlacePoint(
                placeENU.place,
                placeENU.enu,
                (0.5f + projectedPoint[0] / projectedPoint[3]) * projectionMatrix.width,
                (0.5f - projectedPoint[1] / projectedPoint[3]) * projectionMatrix.height,
                -projectedPoint[2],
                locationModel
            )
        }
    }

    fun touching(x: Float, y: Float, selected: Boolean = false): Boolean {
        val circleSize = if (selected) CIRCLE_SIZE * 4 / 3 else CIRCLE_SIZE
        return (x >= this.x - circleSize && x <= this.x + circleSize
                && y >= this.y - circleSize && y <= this.y + circleSize)
    }

    fun draw(
        context: Context,
        canvas: Canvas,
        selected: Boolean = false,
        currentLocation: Location?
    ) {
        if (z > 0) {
            val circleSize = if (selected) CIRCLE_SIZE * 4 / 3 else CIRCLE_SIZE
            canvas.drawCircle(x, y, circleSize.toFloat(), defaultPaint)
            var textToShow = getDistanceText(context, currentLocation)
            if (textToShow.isNotEmpty()) {
                canvas.drawText("$textToShow", x, y + CIRCLE_SIZE + 70, textToShowPaint)
            }
            val iconSize = if (selected) CIRCLE_SIZE else CIRCLE_SIZE * 3 / 4
            if(locationModel.bitmap == null) {
                val icon = ResourcesCompat.getDrawable(
                    context.resources, locationModel.iconID, null
                )
                icon?.let {
                    try {
                        it.setBounds(
                            x.roundToInt() - iconSize,
                            y.roundToInt() - iconSize,
                            x.roundToInt() + iconSize,
                            y.roundToInt() + iconSize
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    it.draw(canvas)
                }
            } else {
                val bmp = locationModel.bitmap
                canvas.drawBitmap(bmp, x - bmp.width/2, y - bmp.height/2, defaultPaint)
            }
        }
    }

    private fun getDistanceText(context: Context, currentLocation: Location?): String {
        val pointLocation = Location("Point")
        pointLocation.latitude = place.locationModel.lati
        pointLocation.longitude = place.locationModel.longi
        val distance = currentLocation!!.distanceTo(pointLocation)
        val distanceText = if (distance < 1000) {
            context.getString(R.string.info_m).format(distance.toInt())
        } else {
            context.getString(R.string.info_km)
                .format(distance.toInt() / 1000)
        }
        return distanceText
    }
}