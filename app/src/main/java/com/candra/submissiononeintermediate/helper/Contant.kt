package com.candra.submissiononeintermediate.helper


import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Contant {

    const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    const val AUTHORIZATION = "Authorization"
    val ID = stringPreferencesKey("id")
    val PASSWORD = stringPreferencesKey("password")
    val EMAIL = stringPreferencesKey("email")
    val TOKEN = stringPreferencesKey("token")
    val LOGGIN = booleanPreferencesKey("boolean")
    val NAME = stringPreferencesKey("name")
    const val TAG = "Add Story"
    const val BARER = "Bearer "

    const val EXTRA_DATA = "data"

}