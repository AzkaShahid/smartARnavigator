package com.app.ar

import android.content.Context
import android.location.Location
import java.time.LocalDateTime

fun interface PlaceListener {
    companion object {
        private val placeListeners = mutableListOf<PlaceListener>()

        fun register(placeListener: PlaceListener) {
            if (placeListeners.all { it != placeListener }) {
                placeListeners.add(placeListener)
            }
        }

        fun unregister(placeListener: PlaceListener) {
            placeListeners.remove(placeListener)
        }

        fun trigger(places: List<Place>?) {
            placeListeners.forEach {
                it.onPlacesChanged(places)
            }
        }
    }

    fun onPlacesChanged(places: List<Place>?)
}

class Place private constructor(
    val id: String,
    val locationModel: LocationModel,
    val type: Type,
    val description: String,
    val timestamp: LocalDateTime?,
    val location: Location
) {
    enum class Type {
        Information,
        Gallery,
        Garden,
        Rides,
        ParkingArea,
        Restroom,
        GiftShop,
        FoodCourt,
        Kaaba,
        Unknown,
    }

    companion object {
        fun fetchPlaces(
            context: Context,
            tagId: String,
            locationModels: ArrayList<LocationModel> = ArrayList()
        ) {
            val places = mutableListOf<Place>()
            for (i in 0 until locationModels.size) {
                val location = Location("any location")
                location.latitude = locationModels[i].lati
                location.longitude = locationModels[i].longi
                val place = Place(
                    "${i + 1}",
                    locationModels[i],
                    Type.Kaaba,
                    "",
                    LocalDateTime.now(),
                    location
                )
                places.add(place)
            }
            PlaceListener.trigger(places)
        }
    }
}