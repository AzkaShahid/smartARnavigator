package com.app.network


import com.app.constants.AppConstants
import com.app.models.countries.CountriesResponseModel
import com.app.models.login.LoginUserResponse
import retrofit2.http.*

/**
 * @author Azka Shahid
 */
interface ApiClientInterface {
    @GET(AppConstants.WebURL.LOGIN)
    suspend fun callLogin(
        @Query("username") userName: String,
        @Query("password") password: String
    ): LoginUserResponse

    @GET()
    suspend fun getCountries(@Url string: String): CountriesResponseModel
}