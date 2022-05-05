package com.candra.submissiononeintermediate.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.candra.submissiononeintermediate.helper.`object`.SettingTheme
import com.candra.submissiononeintermediate.model.local.LoginUpUser
import com.candra.submissiononeintermediate.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel()
{

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _succes = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _succes

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    private val _messageSuccess = MutableLiveData<String>()
    val messageSuccess: LiveData<String> = _messageSuccess


   suspend fun registerAccountData(name: String,email: String,password: String) = viewModelScope.launch {
        _loading.value = true
        val responseData = repository.registerAccount(name,email,password)
        try {
            responseData.let {
                if (it.isSuccessful){
                    _loading.value = false
                    _succes.value = true
                    _messageSuccess.value = "User created"

                }else{
                   if (it.code() == 400){
                       _loading.value = false
                       _succes.value = false
                       _messageError.value = "Email is already taken"
                   }
                }
            }
        }catch (e: Exception){
            _loading.value = false
            _messageError.value= e.message.toString()
            _succes.value = false
        }
    }

    fun isLoadingShow(){
        _loading.value = true
    }

    fun isLoadingGone(){
        _loading.value = false
    }

    fun showErrorMessage(it: String){
        _loading.value = false
        _messageError.value = it
    }

   suspend fun loginAccount(context: Context,email: String,password: String) = viewModelScope.launch {
        _loading.value = true
       try {
           val responseData = repository.loginUser(email = email,password = password)
           responseData.let {
               if (it.isSuccessful && it.body() != null){
                   _loading.value = false
                   _succes.value = true
                   _messageSuccess.value = "Berhasil Login"
                   val result = it.body()?.loginResult
                   result?.let { data ->
                       val user = LoginUpUser(
                           id = data.userId,
                           email = email,
                           password = password,
                           name = data.name,
                           token = data.token,
                           isLogginIn = true
                       )
                       SettingTheme.updateDataUser(context,user)
                   }
               }else{
                   _loading.value = false
                   _succes.value = false
                   when(it.code()){
                       401 -> _messageError.value = "Invalid password"?: "Tidak ditemukan"
                       else -> _messageError.value = it.errorBody().toString()
                   }
               }
           }
       }catch (e: Exception){
           _loading.value = false
           _succes.value = false
           _messageError.value = e.message.toString()
       }
   }

    suspend fun logoutUser(context: Context) = viewModelScope.launch {
        repository.logoutUser(context)
    }

    fun getDataUser(context: Context) = repository.getDataUser(context).asLiveData()

}