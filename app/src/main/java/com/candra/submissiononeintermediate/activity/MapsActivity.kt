package com.candra.submissiononeintermediate.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.databinding.MapsActivityBinding
import com.candra.submissiononeintermediate.helper.Help
import com.candra.submissiononeintermediate.model.LoginUpUser
import com.candra.submissiononeintermediate.viewmodel.MapsViewModel
import com.candra.submissiononeintermediate.viewmodel.PostStoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(),OnMapReadyCallback{

    private lateinit var binding: MapsActivityBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var loginUser: LoginUpUser
    private val postStoryViewModel by viewModels<PostStoryViewModel>()
    private val mapViewModel by viewModels<MapsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val supportGoogleFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment

        supportGoogleFragment.getMapAsync(this)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this@MapsActivity)

        Help.showToolbar(this@MapsActivity,supportActionBar,"Map",2)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
          toListStory()
        }
        return true

    }

    private fun toListStory(){
        startActivity(Intent(this@MapsActivity,ListStroy::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        toListStory()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        googleMap.isMyLocationEnabled = true

        foundDeviceLocation()
        setOptionMapStyle()
        readAllData()
    }


    private fun setOptionMapStyle(){
        try {
           val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this@MapsActivity,
                    R.raw.map_style,
                )
            )

           if (!success){
               Log.d(TAG, "setOptionMapStyle: Error")
           }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private val requestLauncherPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if (it){
            foundDeviceLocation()
        }else{
            Help.showDialogPermissionDenied(this@MapsActivity,getString(R.string.lokasi))
        }
    }


    private fun foundDeviceLocation() {
        if (ContextCompat.checkSelfPermission(
               this@MapsActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            fusedLocation.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
                }
            }

        } else {
            requestLauncherPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun readAllData() {

        mapViewModel.loadingMap.observe(this){
            showLoading(it)
        }

        mapViewModel.errorMessage.observe(this){
            Help.showDialog(this@MapsActivity,it)
        }



        postStoryViewModel.getDataUser(this@MapsActivity).observe(this) {
            this.loginUser = it

            lifecycleScope.launch(Dispatchers.IO){
                mapViewModel.getDataStoryWithLocation(loginUser.token?: "",googleMap,this@MapsActivity)
            }
        }

    }

    private fun showLoading(show: Boolean){
        with(binding){
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
        }
    }


    companion object{
        const val TAG = "MapsActivity"
    }
}