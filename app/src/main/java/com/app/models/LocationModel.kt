package com.app.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class LocationModel(
    var name: String? = "",
    var category: String? = "",
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0
) {
    constructor() : this("", "", 0.0, 0.0)
}

