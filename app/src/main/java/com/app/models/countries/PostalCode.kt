package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class PostalCode(
    @SerializedName("format")
    var format: String? = "",
    @SerializedName("regex")
    var regex: String? = ""
)