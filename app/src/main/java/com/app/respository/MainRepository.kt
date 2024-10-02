package com.app.respository

import com.app.bases.BaseRepository
import com.app.database.AppDao
import com.app.network.ApiClientInterface
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Azka Shahid
 */
@Singleton
class MainRepository @Inject constructor(
    private val apiClientInterface: ApiClientInterface,
    private val appDao: AppDao
) :
    BaseRepository() {

    suspend fun callLogin(
        userName: String,
        password: String
    ) = safeApiCall {
        apiClientInterface.callLogin(userName, password)
    }


    suspend fun callCountries() = safeApiCall {
        apiClientInterface.getCountries("https://restcountries.com/v3.1/independent?status=true")
    }

}