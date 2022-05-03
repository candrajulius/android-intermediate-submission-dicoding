package com.candra.submissiononeintermediate.viewmodel


import android.util.AndroidRuntimeException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.candra.submissiononeintermediate.model.local.MapStory
import com.candra.submissiononeintermediate.model.local.toGenereteMapStory
import com.candra.submissiononeintermediate.repository.RepoKhusus
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

    private var _dataStoryMap = MutableLiveData<List<MapStory>>()
    val dataStoryMap: LiveData<List<MapStory>> = _dataStoryMap


    suspend fun getDataStoryWithLocation(token: String) = viewModelScope.launch {
        _loading.value = true
        repositoryKhusus.getStoryUserWithLocation(token,1,50,1).let {
            if (it.isSuccessful){
                _loading.value = false
                _dataStoryMap.value = it.body()?.listStoryResponse?.toGenereteMapStory()
            }else{
                _loading.value = false
                _errorMessage.value = it.errorBody().toString()
            }
        }
    }
}