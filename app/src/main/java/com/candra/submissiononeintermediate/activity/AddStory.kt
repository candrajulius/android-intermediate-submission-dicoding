package com.candra.submissiononeintermediate.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.databinding.AddStoryActivityBinding
import com.candra.submissiononeintermediate.helper.*
import com.candra.submissiononeintermediate.helper.Help.showDialogPermissionDenied
import com.candra.submissiononeintermediate.model.LoginUpUser
import com.candra.submissiononeintermediate.viewmodel.PostStoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

@AndroidEntryPoint
class AddStory: AppCompatActivity()
{
    private lateinit var binding: AddStoryActivityBinding

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var loginUser: LoginUpUser
    private val storyViewModel: PostStoryViewModel by viewModels()
    private var location: Location? = null
    private lateinit var fusedLocation: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddStoryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Help.showToolbar(this@AddStory,supportActionBar,getString(R.string.tambah_gambar),2)

        setOnClik()

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        requestPermission()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setOnClik(){
        with(binding){
            cameraButton.setOnClickListener {
                cameraClick()
            }
            galleryButton.setOnClickListener {
               galleryClick()
            }
            addImage.setOnClickListener {
                postNewStory()
            }

            readAllData()

        }
    }

    private fun cameraClick(){
        checkPermssionCamera()
    }

    private fun galleryClick(){
        checkPermissionGallery()
    }

    private fun checkPermissionGallery(){
        Dexter.withContext(this@AddStory)
            .withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    Help.showToast(this@AddStory, getString(R.string.gallery_diizinkan))
                    fiturGallery()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).onSameThread().check()
    }

    private fun checkPermssionCamera(){
        Dexter.withContext(this@AddStory)
            .withPermission(
                android.Manifest.permission.CAMERA
            ).withListener(object: PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Help.showToast(this@AddStory,getString(R.string.camera_diizinkan))
                    fiturCamera()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                   showDialogPermissionDenied(this@AddStory,getString(R.string.camera))
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                   showDialogPermissionDenied(this@AddStory,getString(R.string.camera))
                }

            }).onSameThread().check()
    }

    private fun fiturCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTemptFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this@AddStory,
                "com.candra.submissiononeintermediate",
                it
            )

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun fiturGallery(){
        val intent = Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"
        }

        val chooser = Intent.createChooser(intent,getString(R.string.pilih_gambar))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){

            val myFile = File(currentPhotoPath)

            getFile =myFile

            val result = BitmapFactory.decodeFile(getFile?.path)

            binding.gambarAdd.load(result){
                crossfade(true)
                crossfade(600)
                error(R.drawable.ic_baseline_broken_image_24)
                placeholder(R.drawable.ic_baseline_image_24)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val selectedImg: Uri = it.data?.data as Uri

            val myFile = uriToFile(selectedImg,this@AddStory)
            getFile = myFile

            binding.gambarAdd.load(selectedImg){
                crossfade(true)
                crossfade(600)
                error(R.drawable.ic_baseline_broken_image_24)
                placeholder(R.drawable.ic_baseline_image_24)
            }
        }
    }

    private fun postNewStory(){
       val description = binding.textDescription.text.toString().lowercase().trim()
        if (description.isEmpty()){
            binding.textDescription.error = "Deskripsi ${resources.getString(R.string.kosong)}"
        }else{
            if (getFile != null){
                val fileData = reduceFileImage(getFile as File)

                val deskripsi = description.toRequestBody("text/plain".toMediaType())
                val imageRequest = fileData.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageData: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    fileData.name,
                    imageRequest
                )

                var lat: RequestBody? = null
                var lon: RequestBody? = null

                if (location != null){
                    lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                    lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                }

                lifecycleScope.launch {
                    storyViewModel.postStoriesUser(loginUser.token?: "",imageData,deskripsi,lat,lon)
                }

            }else{
                Help.showToast(this@AddStory,getString(R.string.foto_diisi))
            }
        }
    }

    private fun readAllData(){
        storyViewModel.getDataUser(this@AddStory).observe(this){
            this.loginUser = it
        }

        storyViewModel.errorMessage.observe(this){
            Help.showDialog(this@AddStory,it)
        }

        storyViewModel.success.observe(this){
            if (it) toListStoryActivity()
        }

        storyViewModel.loading.observe(this){
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.addImage.visibility = if (it) View.GONE else View.VISIBLE
        }

    }

    private fun toListStoryActivity(){
        finish()
    }

    private fun requestPermission(){
        Dexter.withContext(this@AddStory)
            .withPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION
            ,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0?.areAllPermissionsGranted() == true){
                        accessLocation()
                    }else{
                       showDialogPermissionDenied(this@AddStory,getString(R.string.lokasi))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).onSameThread().check()
    }


    @SuppressLint("MissingPermission")
    private fun accessLocation(){
        fusedLocation.lastLocation.addOnSuccessListener {
            it.let {
                setTextLocation(it)
            }
        }
    }


    private fun setTextLocation(it: Location){
       Geocoder(this, Locale.getDefault()).apply {
           getFromLocation(it.latitude,it.longitude,1).first().let { address ->
               binding.location.text = buildString {
                   append(address.locality).append(",")
                   append(address.subAdminArea)
               }
           }
       }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}