package com.app.ar

import android.graphics.Canvas
import android.hardware.SensorEvent
import android.location.Location
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.app.R
import com.app.databinding.FragmentHomeBinding
import com.app.extention.toGone
import com.app.extention.toVisible
import com.app.ui.sidenavigation.home.HomeFragment


class ArOverlayView(
    private val locationModels: ArrayList<LocationModel>,
    private val fragment: HomeFragment,
    private val views: FragmentHomeBinding,
    compassUpdate: (event: SensorEvent, location: Location?, destinationLocationModel: LocationModel?) -> Unit
) :
    View(fragment.mainActivity) {


    public var selectedPlace: PlacePoint? = null
    public var selectedLocationModel: LocationModel? = null
    private var projectionMatrix: ProjectionMatrix? = null
    private var currentECEF: ECEFCoordinate? = null

    private var places = listOf<Place>()
    private var placeENUs = listOf<PlaceENU>()
    private var placePoints = listOf<PlacePoint>()

    private var selectedPlaceId: String? = null
    private var currentLocation: Location? = null

    private val locationListener = LocationListener { location ->
        currentECEF = ECEFCoordinate.fromLocation(location)
        this.currentLocation = location
        processPlaceENUs()
        if(selectedLocationModel != null){
            updateLocationDistanceDetails(selectedLocationModel)
        }
    }

    private val placeListener = PlaceListener { places ->
        places?.let { safePlaces ->
            this.places = safePlaces
            processPlaceENUs()
        }
    }

    private val rotationListener = RotationListener { event ->
        projectionMatrix = ProjectionMatrix.fromRotationVectorAndLayout(
            event.values, width.toFloat(), height.toFloat()
        )
        var destinationLocation: LocationModel? = null
        if (locationModels.size == 1) {
            destinationLocation = locationModels[0]
        }
        compassUpdate.invoke(event, currentLocation, destinationLocation)
        processPlacePoints()
    }

    private fun updateLocationDistanceDetails(locationModel: LocationModel?) {
        selectedLocationModel = locationModel
        if (locationModel == null || currentLocation == null) {
            views.tvLocationDistance.text = "-"
        } else {
            val pointLocation = Location("Point")
            pointLocation.latitude = locationModel.lati
            pointLocation.longitude = locationModel.longi
            var distance = currentLocation!!.distanceTo(pointLocation).toInt()
            var distanceUnit = fragment.mainActivity.getString(R.string.meter_distance)

            if (distance >= 1000) {
                distance /= 1000
                distanceUnit = fragment.mainActivity.getString(R.string.km_distance)
            }
            views.tvLocationDistance.text = distance.toString()
            views.tvDistanceUnit.text = distanceUnit
        }
    }

    private fun updateLocationDetails(locationModel: LocationModel?) {
        selectedLocationModel = locationModel
        if (locationModel == null) {
            views.tvLocationDistance.text = "-"
        } else {
            if(locationModel.bitmap != null){
//                views.ivLocation.setImageBitmap(locationModel.bitmap)
            } else {
                try{
//                    views.ivLocation.setImageDrawable(ContextCompat.getDrawable(context, locationModel.iconID))
                } catch (ex: Exception){

                }
            }
            views.tvLocationTitle.text = locationModel.name
            if(locationModel.category.isNullOrEmpty()){
                views.cvCategory.toGone()
            } else{
                views.cvCategory.toVisible()
                views.cvCategory.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                views.tvCategory.text = locationModel.category
            }
            if(locationModel.pavilionName.isNullOrEmpty()){
                views.cvPavilion.toGone()
            } else{
                views.cvPavilion.toVisible()
                views.cvPavilion.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                views.tvPavilion.text = locationModel.pavilionName
            }
        }
    }

    private val onTouchListener = OnTouchListener { _, event ->
        event?.let { safeEvent ->
            when (safeEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    performClick()

                    var placePoint: PlacePoint? = null
                    placePoints.forEach {
                        val selected = it.place.id == selectedPlaceId
                        if (it.touching(safeEvent.x, safeEvent.y, selected)) {
                            placePoint = it
                            selectedPlace = placePoint
                        }
                    }

                    selectPlace(placePoint, false)
                }
            }
        }

        false
    }

    private fun processPlaceENUs() {
        currentECEF?.let { safeCurrentEcef ->
            val newPlaceENUs = mutableListOf<PlaceENU>()
            places.forEach {
                newPlaceENUs.add(
                    PlaceENU.fromCurrentECEFToPlace(
                        safeCurrentEcef,
                        it,
                        it.locationModel
                    )
                )
            }

            placeENUs = newPlaceENUs.toList()
            processPlacePoints()
        }
    }

    private fun processPlacePoints() {
        projectionMatrix?.let { safeProjectionMatrix ->
            val newPlacePoints = mutableListOf<PlacePoint>()
            placeENUs.forEach {
                val placePoint =
                    PlacePoint.fromProjectionOfPlaceEnu(safeProjectionMatrix, it, it.locationModel)
                newPlacePoints.add(placePoint)
            }

            placePoints = newPlacePoints
            invalidate()
        }
    }

    public fun selectPlace(placePoint: PlacePoint?, forceHide: Boolean) {
        if (placePoint != null || forceHide) {
            selectedPlaceId =
                if (placePoint!!.place.id != selectedPlaceId) placePoint.place.id else null
            if (selectedPlaceId == null) {
                if (views.cvLocationView.visibility != View.GONE) {
                    hideLocationDetail()
                }

                return
            }

            selectedPlaceId?.let { safeSelectedPlaceId ->
                val placePoint = placePoints.find { it.place.id == safeSelectedPlaceId }

                placePoint?.let { safePlacePoint ->
                    showLocationDetail(placePoint.locationModel)
                }
            }
        }
    }

    public fun hideLocationDetail() {
        selectedLocationModel = null
        views.cvLocationView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slidedown_out))
        views.cvLocationView.visibility = View.GONE
        views.viewMovePadding.visibility = View.VISIBLE
    }

    public fun showLocationDetail(locationModel: LocationModel) {
        updateLocationDistanceDetails(locationModel)
        updateLocationDetails(locationModel)
        views.cvLocationView.visibility = View.VISIBLE
        views.viewMovePadding.visibility = View.GONE
        views.cvLocationView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slidedown_in))
    }

    fun start() {
        LocationListener.register(locationListener)
        PlaceListener.register(placeListener)
        RotationListener.register(rotationListener)

        val tagId = fragment.mainActivity.intent.getStringExtra(fragment.mainActivity.getString(R.string.intent_tag_id))

        tagId?.let {
            Place.fetchPlaces(fragment.mainActivity, it, locationModels)
        }

        if (parent != null) {
            val viewGroup = parent as ViewGroup
            viewGroup.removeView(this)
        }
        views.arFrameLayout.addView(this)

        setOnTouchListener(onTouchListener)
//        llClose.setOnClickListener {
//            hideLocationDetail()
//        }
    }

    fun stop() {
        LocationListener.unregister(locationListener)
        PlaceListener.unregister(placeListener)
        RotationListener.unregister(rotationListener)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.let { safeCanvas ->
            for (i in 0 until placePoints.size) {
//                var distanceText = distanceTexts[i]
                var place = placePoints[i]
                place.draw(context, safeCanvas, place.place.id == selectedPlaceId, currentLocation)
            }
        }
    }
}