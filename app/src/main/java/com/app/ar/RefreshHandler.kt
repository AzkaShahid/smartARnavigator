package com.app.ar

import android.app.Activity
import com.app.models.countries.CountryItemModel

class RefreshHandler constructor(private val activity: Activity, private val locationModels: ArrayList<LocationModel> = ArrayList(),
                                ) {

    private val placeListener = PlaceListener {  }

    fun start() {
        PlaceListener.register(placeListener)
        Place.fetchPlaces(activity, "", locationModels)
    }

    fun stop() {
        PlaceListener.unregister(placeListener)
    }
}