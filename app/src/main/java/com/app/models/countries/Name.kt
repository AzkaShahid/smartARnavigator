package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("common")
    var common: String? = "",
    @SerializedName("nativeName")
    var nativeName: NativeName? = NativeName(),
    @SerializedName("official")
    var official: String? = ""
)