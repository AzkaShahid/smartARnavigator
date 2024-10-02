package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class Maps(
    @SerializedName("googleMaps")
    var googleMaps: String? = "",
    @SerializedName("openStreetMaps")
    var openStreetMaps: String? = ""
)