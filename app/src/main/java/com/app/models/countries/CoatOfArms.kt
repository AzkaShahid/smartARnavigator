package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class CoatOfArms(
    @SerializedName("png")
    var png: String? = "",
    @SerializedName("svg")
    var svg: String? = ""
)