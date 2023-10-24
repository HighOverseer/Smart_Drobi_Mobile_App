package com.smartdrobi.aplikasipkm.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.smartdrobi.aplikasipkm.R
import com.smartdrobi.aplikasipkm.databinding.ActivityMapsBinding
import com.smartdrobi.aplikasipkm.domain.helper.JAKARTA_LAT
import com.smartdrobi.aplikasipkm.domain.helper.JAKARTA_LON
import com.smartdrobi.aplikasipkm.domain.helper.showToast
import com.smartdrobi.aplikasipkm.domain.helper.toDp
import com.smartdrobi.aplikasipkm.ui.addbridge.AddBridgeFormActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding:ActivityMapsBinding
    private lateinit var map:GoogleMap
    private lateinit var geoCoder: Geocoder

    private var settingCurrMarkerJob: Job? = null

    private var currMarker:Marker?=null
        get(){
            if (field == null){
                map.clear()
                field = map.addMarker(
                    MarkerOptions()
                        .position(
                            LatLng(
                                JAKARTA_LAT,
                            JAKARTA_LON
                            )
                        )
                        .title(getString(R.string.jakarta))
                        //.snippet(getString(R.string.ibu_kota_indonesia))
                )
            }
            return field
        }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        if (
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] != true
            || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] != true
        ){
            showToast(this, getString(R.string.location_permission_info))
            finish()
        }else goToCurrLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewComponents()
    }

    private fun initViewComponents(){
        initMaps()
        initButtons()
        geoCoder = Geocoder(this, Locale.getDefault())
    }

    private fun initButtons(){
        binding.apply {

            ibBack.setOnClickListener {
                finish()
            }

            btnFinish.setOnClickListener {
                finishSessionReturnResult()
            }
        }

    }

    private fun finishSessionReturnResult(){
        currMarker?.let {
            val lat = it.position.latitude
            val lon = it.position.longitude
            val markerTitle = it.title
            val mapLocationName = if (markerTitle?.contains(getString(R.string.curr_location)) == true){
                markerTitle.removeRange(0, 16)
            }else markerTitle

            val intent = Intent().apply {
                putExtra(AddBridgeFormActivity.LAT_KEY, lat)
                putExtra(AddBridgeFormActivity.LON_KEY, lon)
                putExtra(AddBridgeFormActivity.LOC_NAME_KEY, mapLocationName)
            }
            setResult(AddBridgeFormActivity.ACTIVITY_MAP_RESULT_CODE, intent)
            finish()
        }?:return
    }

    private fun initMaps(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.container_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.apply {
            uiSettings.apply {
                isMapToolbarEnabled = false
            }
            moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    JAKARTA_LAT,
                    JAKARTA_LON
                ),
                16f
            ))
            setZoomControlPosition()
            goToCurrLocation()
        }

    }

    private fun setCurrMarker(position:LatLng, title:String = ""){
        settingCurrMarkerJob?.cancel()

        settingCurrMarkerJob = lifecycleScope.launch {
            currMarker?.let {


                var locName = convertToAddressLine(
                    position.latitude.toFloat(),
                    position.longitude.toFloat(),
                    geoCoder,
                    getString(R.string.unknown_location_title)
                )

                val isLocationPointOfInterest = title.isNotBlank()

                if (isLocationPointOfInterest){
                    val adressArray = locName.split(", ")
                    val city = adressArray[adressArray.size - 2]
                    val province = adressArray[adressArray.size - 1]
                    locName = "$title, $city, $province"
                }

                it.hideInfoWindow()
                it.position = position
                it.title = locName
               /* it.snippet = getString(
                    R.string.default_location_snippet,
                    position.latitude.toString().substring(0, 7),
                    position.longitude.toString().substring(0, 7)
                )*/
                it.showInfoWindow()
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        position,
                        16f
                    )
                )

            }
        }

    }

    private fun setZoomControlPosition(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.container_map) as SupportMapFragment
        mapFragment.view?.also {
            val zoomControlView = it.findViewById<View>(MAP_ZOOM_CONTROL_ID)
            zoomControlView?.apply {
                val params = (layoutParams as RelativeLayout.LayoutParams)
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                val margin = 10.toDp(resources.displayMetrics)
                params.setMargins(margin, margin, 0, 0)

                map.uiSettings.isZoomControlsEnabled = true
            }
        }
    }


    private fun goToCurrLocation(){
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.apply {
                addOnSuccessListener { location ->
                    location?.let {
                        setCurrMarker(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ),
                            getString(R.string.curr_location)
                        )
                    }
                }
                setMapClickListeners()
            }

        }else{
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun setMapClickListeners(){
        map.apply {
            setOnMapClickListener {
                setCurrMarker(
                    it
                )
            }
            setOnPoiClickListener {
                setCurrMarker(
                    it.latLng,
                    it.name
                )
            }
        }
    }

    private fun checkPermission(permission:String):Boolean{
        return ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun convertToAddressLine(
        lat: Float?,
        lon: Float?,
        geocoder: Geocoder,
        addressNameDefault: String
    ) = withContext(
        Dispatchers.IO
    ) {
        var addressName = addressNameDefault
        if (lat != null && lon != null) {
            try {
                val list = geocoder.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
                if (list != null && list.size != 0) {
                    val thoroughFare = list[0].thoroughfare
                    addressName = if (thoroughFare != null){
                        "${thoroughFare}, ${list[0].locality}, ${list[0].subAdminArea}, ${list[0].adminArea}"
                    }else "${list[0].locality}, ${list[0].subAdminArea}, ${list[0].adminArea}"

                    println(addressName)
                    addressName
                } else {
                    addressName
                }
            } catch (e: Exception) {
                e.printStackTrace()
                addressName
            }
        } else addressName
    }

    companion object{
        private const val MAP_ZOOM_CONTROL_ID = 0x1
    }


}