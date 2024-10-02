package com.app.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.app.R
import com.bumptech.glide.Glide

object PopupUtils {

    public fun showCalibratePopup(v: View, dismissUnit: (dismiss: Boolean) -> Unit) {
        val popupWindow = PopupWindow(
            View.inflate(v.context, R.layout.popup_calibrate, null),
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )
        val ivCalibrate: ImageView = popupWindow.contentView.findViewById(R.id.ivCalibrate)
        val btnOK: Button = popupWindow.contentView.findViewById(R.id.btnOK)
        Glide.with(v.context).asGif().load(R.raw.compass_calibration).into(ivCalibrate)
        btnOK.setOnClickListener { popupWindow.dismiss() }
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0)
        dimBehind(popupWindow)
    }

    public fun dimBehind(popupWindow: PopupWindow) {
        val container = popupWindow.contentView.rootView
        val context = popupWindow.contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container?.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.5f
        wm.updateViewLayout(container, p)
    }
}