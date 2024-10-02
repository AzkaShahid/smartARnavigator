package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class Cat(
    @SerializedName("common")
    var common: String? = "",
    @SerializedName("official")
    var official: String? = ""
)