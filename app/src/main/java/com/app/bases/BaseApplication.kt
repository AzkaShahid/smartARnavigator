package com.app.bases

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @author Azka Shahid
 */
@HiltAndroidApp
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}