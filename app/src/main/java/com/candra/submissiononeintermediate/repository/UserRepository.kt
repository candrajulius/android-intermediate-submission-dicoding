package com.candra.submissiononeintermediate.repository

import android.content.Context
import com.candra.submissiononeintermediate.api.ApiInterface
import com.candra.submissiononeintermediate.helper.`object`.SettingTheme
import com.candra.submissiononeintermediate.model.local.LoginUpUser
import javax.inject.Inject

class UserRepository@Inject constructor(
    private val retrofit: ApiInterface
){

  suspend fun registerAccount(name: String,email: String,password: String) = retrofit.registerUser(name,email,password)

  suspend fun loginUser(email: String,password: String) = retrofit.loginUsername(email,password)

  suspend fun logoutUser(context: Context) = SettingTheme.updateDataUser(context,
      LoginUpUser(token = null,isLogginIn = false)
  )

  fun getDataUser(context: Context) = SettingTheme.getDataUser(context = context)

}