package com.candra.submissiononeintermediate.viewmodel


import android.content.Context
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.submissiononeintermediate.R
import com.candra.submissiononeintermediate.helper.vectorToBitmap
import com.candra.submissiononeintermediate.repository.RepoKhusus
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repositoryKhusus: RepoKhusus,
) : ViewModel()
{
    private var _loading = MutableLiveData<Boolean>()
    val loadingMap: LiveData<Boolean> = _loading

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    suspend fun getDataStoryWithLocation(token: String,googleMap: GoogleMap,context: Context) = viewModelScope.launch {
        _loading.value = true
        repositoryKhusus.getStoryUserWithLocation(token,1,50,1).let {
            if (it.isSuccessful){
                _loading.value = false
                it.body()?.listStoryResponse?.forEach { story ->
                   val latLang = LatLng(story.lat,story.lon)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(latLang)
                            .title(story.name)
                            .snippet(story.description)
                            .icon(vectorToBitmap(R.drawable.person_pin_circle_48px,Color.RED,context))
                    )
                }
            }else{
                _loading.value = false
                _errorMessage.value = it.errorBody().toString()
            }
        }
    }
}