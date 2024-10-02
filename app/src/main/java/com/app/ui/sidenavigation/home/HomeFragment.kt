package com.app.ui.sidenavigation.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.Location
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.activities.SearchActivity
import com.app.ar.ArOverlayView
import com.app.ar.LocationHandler
import com.app.ar.LocationModel
import com.app.ar.RefreshHandler
import com.app.ar.RotationHandler
import com.app.bases.BaseFragment
import com.app.databinding.FragmentHomeBinding
import com.app.preferences.PreferencesHelper
import com.app.ui.adapters.SearchAdapter
import com.app.utils.AppUtils
import com.app.utils.LocationsManager
import com.bumptech.glide.Glide
import java.util.Locale
import kotlin.math.atan2


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private var mediaPlayer: MediaPlayer? = null
    override val mViewModel: HomeViewModel by viewModels()
    private val SPEECH_REQUEST_CODE = 123
    private val SEARCH_REQUEST_CODE = 456


    private val speechPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startSpeechRecognition()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    private val arPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA
    )

    private val arPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationPermissionGranted = permissions.getOrDefault(
            Manifest.permission.ACCESS_FINE_LOCATION,
            false
        ) || permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        val cameraPermissionGranted = permissions.getOrDefault(Manifest.permission.CAMERA, false)
        if (locationPermissionGranted && cameraPermissionGranted) {
            startCamera()
        }
    }
    var locationModels = ArrayList<LocationModel>()
    lateinit var adapter: SearchAdapter

    val threashHold = 20
    private var preview: Preview? = null

    private var arOverlayView: ArOverlayView? = null
    private var locationHandler: LocationHandler? = null
    private var rotationHandler: RotationHandler? = null
    private var refreshHandler: RefreshHandler? = null
    var goStraightPlaying = false
    var turnLeftPlaying = false
    var turnRightPlaying = false




    override fun getToolbarBinding() = null

    override fun getToolbarTitle() = R.string.menu_home

    override fun isMenuButton() = true


    override fun setupUI(savedInstanceState: Bundle?) {


        Glide.with(this).asGif().load(R.raw.arrow).into(mViewBinding.ivMoveLeft)
        Glide.with(this).asGif().load(R.raw.arrow).into(mViewBinding.ivMoveRight)
        if (AppUtils.checkLocationsIfPermissionsGranted(mainActivity) && AppUtils.checkPermissionGranted(
                mainActivity,
                Manifest.permission.CAMERA
            )
        ) {
            startCamera()
        } else {
            requestForPermissions()
        }
        adapter = SearchAdapter(mainActivity, ArrayList()) {
            showSingleLocationNavigation(it)
        }
        mViewBinding.rvSearch.adapter = adapter

        val model: LocationModel? = arguments?.getParcelable("SEARCH_RESULT")
        model?.let {

            Handler(Looper.getMainLooper()).postDelayed({
                showSingleLocationNavigation(it)

            }, 1000)
        }
//        val model1: LocationModel?= arguments?.getParcelable("COUNTRY_RESULT")
//        model1?.let {
//            Handler(Looper.getMainLooper()).postDelayed({
//                showSingleLocationNavigation(it)
//
//            },1000)
//        }

    }



    private fun showSingleLocationNavigation(model: LocationModel) {
        try {
            mViewBinding.etSearch.setText(model.name)
            AppUtils.hideSoftKeyboard(mainActivity, mViewBinding.etSearch)
            mViewBinding.etSearch.clearFocus()
            adapter.updateAdapter(ArrayList())
            val singleItemList = ArrayList<LocationModel>()
            singleItemList.add(model)
            loadAR(singleItemList)
            arOverlayView!!.selectedLocationModel = model
            arOverlayView!!.showLocationDetail(model)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(mainActivity, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let { dataIntent ->
                    if (dataIntent.hasExtra("SEARCH_RESULT")) {
                        val model: LocationModel? =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                dataIntent.getParcelableExtra(
                                    "SEARCH_RESULT",
                                    LocationModel::class.java
                                )
                            } else {
                                dataIntent.getParcelableExtra("SEARCH_RESULT")
                            }

                        model?.let {
                            showSingleLocationNavigation(it)
                        }

                    }
                }

            }
        }


    private fun requestForPermissions() {
        arPermissionRequest.launch(
            arPermissions
        )

    }

    public fun checkAndHideLocationDetails() {
        if (mViewBinding.cvLocationView.visibility == View.VISIBLE) {
            try {
                if (arOverlayView?.selectedPlace != null) {
                    arOverlayView!!.selectPlace(arOverlayView!!.selectedPlace, true)
                } else {
                    if (arOverlayView != null) {
                        arOverlayView!!.hideLocationDetail()
                    }
                }
            } catch (ex: Exception) {
                if (arOverlayView != null) {
                    arOverlayView!!.hideLocationDetail()
                }
            }
        }
    }

    private fun startCamera() {
        populateLocationModels()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(mainActivity)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            preview = Preview.Builder().build()
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            preview!!.setSurfaceProvider(mViewBinding.viewFinder.createSurfaceProvider())
            cameraProvider.bindToLifecycle(activity as LifecycleOwner, cameraSelector, preview)

        }, ContextCompat.getMainExecutor(mainActivity))

    }

    private fun populateLocationModels() {
        locationModels = LocationsManager.getArLocations(mainActivity)
        loadAR(locationModels)
    }

    override fun attachListener() {
        mViewBinding.etSearch.addTextChangedListener {
            var colorSearchDrawable = R.color.white
            if (mViewBinding.etSearch.text.isEmpty()) {
                mViewBinding.cvSearch.setCardBackgroundColor(
                    ContextCompat.getColor(
                        mainActivity,
                        R.color.color_search_alpha
                    )
                )
            } else {
                colorSearchDrawable = R.color.colorDarkText
                mViewBinding.cvSearch.setCardBackgroundColor(
                    ContextCompat.getColor(
                        mainActivity,
                        R.color.white
                    )
                )
            }
            for (drawable in mViewBinding.etSearch.compoundDrawables) {
                if (drawable != null) {
                    drawable.colorFilter =
                        PorterDuffColorFilter(
                            ContextCompat.getColor(mainActivity, colorSearchDrawable),
                            PorterDuff.Mode.SRC_IN
                        )
                }
            }
        }

        mViewBinding.ivClose.setOnClickListener { mViewBinding.etSearch.text.clear() }

        mViewBinding.etSearch.addTextChangedListener {
            var list = ArrayList<LocationModel>()
            if (mViewBinding.etSearch.text.toString().isEmpty()) {
                mViewBinding.ivClose.visibility = View.GONE
                loadAR(locationModels)
            } else {
                list = ArrayList(locationModels.filter {
                    it.name.contains(
                        mViewBinding.etSearch.text.toString(),
                        true
                    )
                })
                mViewBinding.ivClose.visibility = View.VISIBLE
            }
            adapter.updateAdapter(list)
        }
        mViewBinding.llCloseLocationDetails.setOnClickListener {
            checkAndHideLocationDetails()
        }
        mViewBinding.btnLocation.setOnClickListener {
            openGoogleMaps()
        }
        mViewBinding.ivMic.setOnClickListener {
            startSpeechRecognition()
        }
        mViewBinding.ivMic.setOnClickListener {
            checkAndRequestSpeechPermission()
        }
        mViewBinding.micIcon.setOnClickListener {
            startSpeechRecognition()
        }
        mViewBinding.micIcon.setOnClickListener {
            checkAndRequestSpeechPermission()
        }
        mViewBinding.searchIcon.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            activityResultLauncher.launch(intent)

        }


    }

    private fun checkAndRequestSpeechPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startSpeechRecognition()
            }

            else -> {
                speechPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startSpeechRecognition() {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        startActivityForResult(speechRecognizerIntent, SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (results != null && results.isNotEmpty()) {
                val spokenText = results[0]
                mViewBinding.etSearch.setText(spokenText)
            }
        }
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mViewBinding.micIcon.visibility = View.GONE
            mViewBinding.searchIcon.visibility = View.GONE
            mViewBinding.cvSearch.visibility = View.VISIBLE

        }
    }


    private fun openGoogleMaps() {
        val selectedLocation = arOverlayView?.selectedLocationModel
        if (selectedLocation != null) {
            val locationUri =
                "geo:${selectedLocation.lati},${selectedLocation.longi}?q=${selectedLocation.name}"
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUri))
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {

                Toast.makeText(
                    requireContext(),
                    "Google Maps app is not installed on your device.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "No location selected.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun observeViewModel() {
    }

    private fun loadAR(locationModels: ArrayList<LocationModel>) {
        try {
            arOverlayView = ArOverlayView(
                locationModels,
                this,
                mViewBinding
            ) { event, location, destinationLocationModel ->
                try {
                    calculateDistance(
                        event,
                        location,
                        destinationLocationModel
                    )
                } catch (ex: Exception) {

                }
            }

            locationHandler = LocationHandler(mainActivity)
            rotationHandler = RotationHandler(mainActivity)
            refreshHandler = RefreshHandler(mainActivity, locationModels)
            arOverlayView?.start()
            locationHandler?.start()
            rotationHandler?.start()
            refreshHandler?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateDistance(
        event: SensorEvent,
        location: Location?,
        destinationLocationModel: LocationModel?
    ) {
        if (destinationLocationModel == null) {
            mViewBinding.ivMoveLeft.visibility = View.GONE
            mViewBinding.ivMoveRight.visibility = View.GONE
            mViewBinding.tvDirectionToMove.text = ""
            mViewBinding.ivDirectionToMove.setImageResource(0)
        } else {
            val rotationMatrix = FloatArray(9)
            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                val (matrixColumn, sense) = when (val rotation =
                    mainActivity.windowManager.defaultDisplay.rotation
                ) {
                    Surface.ROTATION_0 -> Pair(0, 1)
                    Surface.ROTATION_90 -> Pair(1, -1)
                    Surface.ROTATION_180 -> Pair(0, -1)
                    Surface.ROTATION_270 -> Pair(1, 1)
                    else -> error("Invalid screen rotation value: $rotation")
                }
                val x = sense * rotationMatrix[matrixColumn]
                val y = sense * rotationMatrix[matrixColumn + 3]
                val atan2 = -atan2(y, x)
                var bearing = Math.toDegrees(atan2.toDouble())
                if (bearing < 0) {
                    bearing += 360
                }
                var locationBearing = 0f
                if (location != null) {

                    if (destinationLocationModel != null) {
                        val destLoc = Location("Service Provider")
                        destLoc.latitude = destinationLocationModel!!.lati
                        destLoc.longitude = destinationLocationModel!!.longi

                        locationBearing = location.bearingTo(destLoc)
                        if (locationBearing < 0) {
                            locationBearing += 360
                        }
                        var leftDis = 0.0
                        var rightDis = 0.0
                        leftDis = if (locationBearing < bearing) {
                            bearing - locationBearing
                        } else {
                            bearing + 360 - locationBearing
                        }
                        rightDis = if (locationBearing > bearing) {
                            locationBearing - bearing
                        } else {
                            bearing + 360 - locationBearing
                        }
                        if (leftDis < rightDis && leftDis > threashHold) {
//                            SoundManager.getInstance(mainActivity).playSoundMoveLeft()
                            playSound(R.raw.turn_left)
                            mViewBinding.ivMoveLeft.visibility = View.VISIBLE
                            mViewBinding.ivMoveRight.visibility = View.GONE
                            mViewBinding.tvDirectionToMove.text = getString(R.string.turn_left)
                            mViewBinding.ivDirectionToMove.setImageDrawable(
                                ContextCompat.getDrawable(
                                    mainActivity,
                                    R.drawable.ic_turn_left
                                )
                            )
                        } else if (rightDis < leftDis && rightDis > threashHold) {
//                            SoundManager.getInstance(mainActivity).playSoundMoveRight()
                            playSound(R.raw.turn_right)
                            mViewBinding.ivMoveLeft.visibility = View.GONE
                            mViewBinding.ivMoveRight.visibility = View.VISIBLE
                            mViewBinding.tvDirectionToMove.text = getString(R.string.turn_right)
                            mViewBinding.ivDirectionToMove.setImageDrawable(
                                ContextCompat.getDrawable(
                                    mainActivity,
                                    R.drawable.ic_turn_right
                                )
                            )
                        } else {
//                            SoundManager.getInstance(mainActivity).playSoundGoStraight()
                            playSound(R.raw.go_straight)
                            mViewBinding.ivMoveLeft.visibility = View.GONE
                            mViewBinding.ivMoveRight.visibility = View.GONE
                            mViewBinding.tvDirectionToMove.text = ""
                            mViewBinding.ivDirectionToMove.setImageResource(0)
                        }


                    }


                }
            }
        }
    }

    private fun playSound(resID: Int) {
        if (PreferencesHelper.isSoundEnabled()) {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    return
                }
            }
            mediaPlayer = MediaPlayer.create(mainActivity, resID)
            mediaPlayer!!.start()
        }

    }

    override fun onResume() {
        super.onResume()
        arOverlayView?.start()
        locationHandler?.start()
        rotationHandler?.start()
        refreshHandler?.start()
    }

    override fun onPause() {
        super.onPause()
        stopMediaPlayer()
        arOverlayView?.stop()
        locationHandler?.stop()
        refreshHandler?.stop()
    }

    private fun stopMediaPlayer() {
        mediaPlayer?.let {
            try {
                it.pause()
                it.release()
                mediaPlayer = null
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}
