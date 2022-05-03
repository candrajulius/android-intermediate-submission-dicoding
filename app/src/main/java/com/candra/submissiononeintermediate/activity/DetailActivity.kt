package com.candra.submissiononeintermediate.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.RoundedCornersTransformation
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.databinding.DetailActivityBinding
import com.candra.submissiononeintermediate.helper.`object`.Contant.EXTRA_DATA
import com.candra.submissiononeintermediate.helper.`object`.Contant.EXTRA_MAP
import com.candra.submissiononeintermediate.helper.`object`.Contant.POSITION
import com.candra.submissiononeintermediate.helper.`object`.Help
import com.candra.submissiononeintermediate.helper.convertLatLngToAddressForAdapter
import com.candra.submissiononeintermediate.helper.genereteDate
import com.candra.submissiononeintermediate.model.local.MapStory
import com.candra.submissiononeintermediate.room.entity.Story
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity: AppCompatActivity(),OnMapReadyCallback
{

    private lateinit var binding: DetailActivityBinding
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Help.showToolbar(this@DetailActivity,supportActionBar,getString(R.string.detail_cerita),2)

        val mapFragment = supportFragmentManager.findFragmentById(binding.mapFragment.id) as SupportMapFragment

        mapFragment.getMapAsync(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }



    private fun getDataFromAdapter(){

        val position = intent.getIntExtra(POSITION,0)

        if (position == 1){
            showDetailStory()
        }else if (position == 2){
            showMapStory()
        }

    }


    private fun showMapStory(){
        intent.getParcelableExtra<MapStory>(EXTRA_MAP)?.let {
            with(binding){

                if (it.lat != 0.0 && it.lon != 0.0){
                    val latLang = LatLng(it.lat,it.lon)
                    googleMap.apply {
                        addMarker(
                            MarkerOptions().position(latLang).title(it.name)
                        )
                        animateCamera(CameraUpdateFactory.newLatLngZoom(latLang,17F))
                    }
                }else{
                    val latlng = LatLng(3.6074169984926407, 98.6821810899946)
                    googleMap.apply {
                        addMarker(
                            MarkerOptions().position(latlng).title(getString(R.string.lokas_not_found))
                        )
                        animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,17F))
                    }
                }

                gambarDetail.load(it.photoUrl){
                    crossfade(true)
                    crossfade(600)
                    transformations(RoundedCornersTransformation())
                }
                textUsername.text = it.name
                textDate.text = it.createdAt.genereteDate
                textDeskripsi.text = it.description
                textLocation.text = convertLatLngToAddressForAdapter(it.lat,it.lon,this@DetailActivity)
            }
        }
    }

    private fun showDetailStory(){
        intent.extras?.getParcelable<Story>(EXTRA_DATA)?.let {
            with(binding){

                if (it.lat != 0.0 && it.lon != 0.0){
                    val latLang = LatLng(it.lat,it.lon)
                    googleMap.addMarker(
                        MarkerOptions().position(latLang).title(it.name)
                    )
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang,17F))
                }else{
                    val latlng = LatLng(3.6074169984926407, 98.6821810899946)
                    googleMap.apply {
                        addMarker(
                            MarkerOptions().position(latlng).title(getString(R.string.lokas_not_found))
                        )
                        animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,17F))
                    }
                }

                gambarDetail.load(it.photoUrl){
                    crossfade(true)
                    crossfade(600)
                    transformations(RoundedCornersTransformation())
                }
                textUsername.text = it.name
                textDate.text = it.createdAt.genereteDate
                textDeskripsi.text = it.description
                textLocation.text = convertLatLngToAddressForAdapter(it.lat,it.lon,this@DetailActivity)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isRotateGesturesEnabled = true
        }
        getDataFromAdapter()
    }
}