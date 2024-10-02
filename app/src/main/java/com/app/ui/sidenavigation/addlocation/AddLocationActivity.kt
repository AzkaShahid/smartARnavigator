package com.app.ui.sidenavigation.addlocation

import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.app.R
import com.app.ar.LocationModel
import com.app.bases.BaseActivity
import com.app.databinding.AddLocationLayoutBinding
import com.app.ui.main.MainViewModel
import com.app.ui.sidenavigation.profile.OnDeleteClickListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase

class AddLocationActivity : BaseActivity<AddLocationLayoutBinding, MainViewModel>() {
    override val mViewModel: MainViewModel by viewModels()
    val locationList = ArrayList<LocationModel>()
    var deleteClickListener: OnDeleteClickListener? = null
    val delayMillis: Long = 800


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val database = FirebaseDatabase.getInstance()
    val locationRef = database.getReference("locations")

    val location = LocationModel("My Location", "Some Category", 123.456, 789.012)

    override fun initBinding() = AddLocationLayoutBinding.inflate(layoutInflater)

    override fun initViews(savedInstanceState: Bundle?) {
        setupActionBar()
    }

    override fun addViewModelObservers() {
    }

    override fun attachListens() {
        mViewBinding.getLocation.setOnClickListener {

            getLastKnownLocation()
        }
        mViewBinding.saveButton.setOnClickListener {
            saveLocationToFirebase()
        }
        mViewBinding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(mViewBinding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
    }

    private fun saveLocationToFirebase() {
        val name = mViewBinding.editName.text.toString()
        val category = mViewBinding.editCategory.text.toString()
        val latitude = mViewBinding.editLat.text.toString().toDoubleOrNull()
        val longitude = mViewBinding.editLong.text.toString().toDoubleOrNull()

        if (name.isNotEmpty() && category.isNotEmpty() && latitude != null && longitude != null) {
            val location = LocationModel(name, category, latitude, longitude)
            val key = locationRef.push().key
            key?.let {
                locationRef.child(it).setValue(location)
                Toast.makeText(this, "Location saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }


    }

    private fun getLastKnownLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        mViewBinding.progressBar.visibility = View.VISIBLE
                        Handler().postDelayed({
                            mViewBinding.progressBar.visibility = View.GONE
                            mViewBinding.editLat.setText(latitude.toString())
                            mViewBinding.editLong.setText(longitude.toString())
                        }, delayMillis)


                    } else {
                        Toast.makeText(this, "Location is not available", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()

            Toast.makeText(this, "Location is not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun delay() {

    }
}