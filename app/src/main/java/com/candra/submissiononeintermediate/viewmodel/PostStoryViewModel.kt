package com.candra.submissiononeintermediate.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.candra.submissiononeintermediate.model.Story
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
    private val userRepository: UserRepository,
): ViewModel()
{

    private val _loading = MutableLiveData<Boolean>()
    var loading: LiveData<Boolean> = _loading


    private val _errorMessage = MutableLiveData<String>()
    var errorMessage: LiveData<String> = _errorMessage

    private val _success = MutableLiveData<Boolean>()
    var success: LiveData<Boolean> = _success

    fun getDataUser(context: Context) = userRepository.getDataUser(context).asLiveData()

   fun dataStory(context: Context): LiveData<PagingData<Story>> = getDataUser(context).switchMap {
           storyRepositoryUser.getStory(it.token ?: "").cachedIn(viewModelScope)
   }

    suspend fun postStoriesUser(token: String,file: MultipartBody.Part,description: RequestBody,lat: RequestBody?,lon: RequestBody?) =
        viewModelScope.launch {
            _loading.value = true
            try {
                storyRepositoryUser.postStory(token,file,description,lat,lon).let {
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


}