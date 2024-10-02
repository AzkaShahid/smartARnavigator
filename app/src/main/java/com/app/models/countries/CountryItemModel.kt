package com.app.models.countries


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CountryItemModel(
    @SerializedName("area")
    var area: Double? = 0.0,
    @SerializedName("borders")
    var borders: List<String?>? = listOf(),
    @SerializedName("capital")
    var capital: List<String>? = listOf(),
    @SerializedName("capitalInfo")
    var capitalInfo: CapitalInfo? = CapitalInfo(),
    @SerializedName("car")
    var car: Car? = Car(),
    @SerializedName("coatOfArms")
    var coatOfArms: CoatOfArms? = CoatOfArms(),
    @SerializedName("continents")
    var continents: List<String?>? = listOf(),
    @SerializedName("flag")
    var flag: String? = "",
    @SerializedName("flags")
    var flags: Flags? = Flags(),
    @SerializedName("independent")
    var independent: Boolean? = false,
    @SerializedName("landlocked")
    var landlocked: Boolean? = false,
    @SerializedName("languages")
    var languages: Languages? = Languages(),
    @SerializedName("latlng")
    var latlng: List<Double>? = listOf(),
    @SerializedName("maps")
    var maps: Maps? = Maps(),
    @SerializedName("name")
    var name: Name? = Name(),
    @SerializedName("population")
    var population: Int? = 0,
    @SerializedName("postalCode")
    var postalCode: PostalCode? = PostalCode(),
    @SerializedName("region")
    var region: String? = "",
    @SerializedName("startOfWeek")
    var startOfWeek: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("subregion")
    var subregion: String? = "",
    @SerializedName("timezones")
    var timezones: List<String?>? = listOf(),
    @SerializedName("unMember")
    var unMember: Boolean? = false
)