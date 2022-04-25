package com.candra.submissiononeintermediate.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.candra.submissiononeintermediate.model.Story
import com.candra.submissiononeintermediate.model.toGenereteListStory
import com.candra.submissiononeintermediate.repository.StoryRepositoryUser
import com.candra.submissiononeintermediate.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PostStoryViewModel @Inject constructor(
    private  val storyRepositoryUser: StoryRepositoryUser,
    private val userRepository: UserRepository
): ViewModel()
{

    private val _loading = MutableLiveData<Boolean>()
    var loading: LiveData<Boolean> = _loading

    val storyMutableliveData = MutableLiveData<List<Story>>()

    private val _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private val _mutableListEmpty = MutableLiveData<Boolean>()
    var mutableListEmpty: LiveData<Boolean> = _mutableListEmpty

    private val _success = MutableLiveData<Boolean>()
    var success: LiveData<Boolean> = _success


    suspend fun getAllStories(token: String) = viewModelScope.launch {
        _loading.value = true
        try{
            val responseData = storyRepositoryUser.getStory(token)
            responseData.let {
                if (it.isSuccessful){
                    _loading.value = false
                    it.body()?.listStoryResponse.let { story ->
                        _mutableListEmpty.value = story?.isEmpty()
                        storyMutableliveData.value = story?.toGenereteListStory()
                    }
                }else{
                    _loading.value = false
                    _errorMessage.value = it.errorBody().toString()
                }
            }
        }catch (e: Exception){
            _loading.value = false
            _errorMessage.value = e.message.toString()
        }

    }

    suspend fun postStoriesUser(token: String,file: MultipartBody.Part,description: RequestBody) =
        viewModelScope.launch {
            _loading.value = true
            try {
                storyRepositoryUser.postStory(token,file,description).let {
                    if (it.isSuccessful){
                        _loading.value = false
                        _success.value = true
                    }else{
                        _loading.value = false
                        _success.value = false
                        _errorMessage.value = "${it.code().toString()} => ${it.errorBody().toString()}"
                    }
                }
            }catch (e: Exception){
                _loading.value = false
                _errorMessage.value = e.message.toString()
            }
        }

    fun getDataUser(context: Context) = userRepository.getDataUser(context).asLiveData()
}