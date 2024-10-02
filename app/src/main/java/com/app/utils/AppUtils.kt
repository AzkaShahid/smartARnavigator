package com.app.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityCompat

object AppUtils {

    fun checkPermissionGranted(context: Context, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkLocationsIfPermissionsGranted(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            ||
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun hideSoftKeyboard(activity: Activity, vararg edittexts: EditText?) {
        try {
            val view = activity.currentFocus
            if (view != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (ex: Exception) {
            var err = ex
        }
    }
}