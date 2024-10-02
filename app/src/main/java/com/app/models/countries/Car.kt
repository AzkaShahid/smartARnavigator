package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("side")
    var side: String? = "",
    @SerializedName("signs")
    var signs: List<String?>? = listOf()
)