package com.app.models.countries


import com.google.gson.annotations.SerializedName

data class NativeName(
    @SerializedName("cat")
    var cat: Cat? = Cat()
)