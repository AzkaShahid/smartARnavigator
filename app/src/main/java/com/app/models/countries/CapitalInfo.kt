package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class CapitalInfo(
    @SerializedName("latlng")
    var latlng: List<Double?>? = listOf()
)