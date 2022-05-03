package com.candra.submissiononeintermediate.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.adapter.MapsAdapter
import com.candra.submissiononeintermediate.databinding.MapsActivityBinding
import com.candra.submissiononeintermediate.helper.`object`.Help
import com.candra.submissiononeintermediate.helper.convertLatLngToAddress
import com.candra.submissiononeintermediate.helper.convertLatLngToAddressForAdapter
import com.candra.submissiononeintermediate.helper.vectorToBitmap
import com.candra.submissiononeintermediate.model.local.LoginUpUser
import com.candra.submissiononeintermediate.model.local.MapStory
import com.candra.submissiononeintermediate.viewmodel.MapsViewModel
import com.candra.submissiononeintermediate.viewmodel.PostStoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
    private var currentMarker = mutableListOf<Marker>()
    private lateinit var markerClickListener: GoogleMap.OnMarkerClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val supportGoogleFragment = supportFragmentManager.findFragmentById(binding.maps.id) as SupportMapFragment

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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }


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

    private fun showShimmerEffect(){
        binding.apply {
            shimmerEffect.startShimmer()
            shimmerEffect.visibility = View.VISIBLE
            rvMap.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideShimmerEffect(){
        binding.apply {
            shimmerEffect.hideShimmer()
            shimmerEffect.visibility = View.GONE
            rvMap.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
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
                    googleMap.isMyLocationEnabled = true
                }else{
                    Help.showDialogPermissionDenied(this@MapsActivity,getString(R.string.lokasi))
                }
            }

        } else {
            requestLauncherPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun descriptionSnippetMyLocation(location: Location): String{
        return buildString { append(convertLatLngToAddress(location.latitude,location.longitude,this@MapsActivity)) }
    }

    private fun readAllData() {

        mapViewModel.loadingMap.observe(this){
           if (it) showShimmerEffect() else hideShimmerEffect()
        }

        mapViewModel.errorMessage.observe(this){
            Help.showDialog(this@MapsActivity,it)
        }

        mapViewModel.dataStoryMap.observe(this){
            locationStory(it)
        }

        postStoryViewModel.getDataUser(this@MapsActivity).observe(this) {
            this.loginUser = it

            lifecycleScope.launch(Dispatchers.IO){
                mapViewModel.getDataStoryWithLocation(loginUser.token?: "")
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        toListStory()
    }

    private fun showReyclerViewInMaps(list: List<MapStory>) {
        binding.rvMap.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = MapsAdapter(this@MapsActivity,::onClickedItem,list)
        }
    }

    private fun onClickedItem(position: Int){
        markerClickListener.onMarkerClick(currentMarker[position])
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker[position].position,17F))
    }


   private fun locationStory(data: List<MapStory>) {
        data.forEach {
            val coordinate = LatLng(it.lat,it.lon)
            val options = MarkerOptions().position(coordinate).title(it.name).snippet(buildString {
                append(convertLatLngToAddressForAdapter(it.lat,it.lon,this@MapsActivity))
            }).icon(vectorToBitmap(R.drawable.person_pin_circle_48px,Color.RED,this@MapsActivity))
            val markerAdd = googleMap.addMarker(options)
            markerAdd?.let { it1 -> currentMarker.add(it1) }
            markerClickListener = GoogleMap.OnMarkerClickListener { n ->
                moveToScrollPosition(n.title?:  "",data)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(n.position,14F))
                false
            }
            googleMap.setOnMarkerClickListener(markerClickListener)

        }
       showReyclerViewInMaps(data)
    }

    private fun moveToScrollPosition(title: String,data: List<MapStory>) {
        val storiesMap = data.find { it.name == title }
        val position = data.indexOf(storiesMap)

        binding.rvMap.apply {
            try {
                post { smoothScrollToPosition(position) }
            }catch (e: Exception){
                Log.d(TAG, "moveToScrollPosition: ${e.message.toString()}")
            }
        }
    }

    companion object{
        const val TAG = "MapsActivity"
    }
}

