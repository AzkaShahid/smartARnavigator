package com.app.ui.sidenavigation.profile

import android.view.View
import com.app.models.LocationModel

interface OnDeleteClickListener {
    fun onDeleteClick(view: View, location: LocationModel)
}