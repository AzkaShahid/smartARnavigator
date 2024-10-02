package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class Flags(
    @SerializedName("alt")
    var alt: String? = "",
    @SerializedName("png")
    var png: String? = "",
    @SerializedName("svg")
    var svg: String? = ""
)