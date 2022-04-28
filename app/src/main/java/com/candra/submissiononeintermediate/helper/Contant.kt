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
    const val DATABSE_NAME = "story_database"
    const val API_EMAIL = "email"
    const val API_NAME = "name"
    const val API_PASSWORD = "password"
    const val API_REGISTER = "register"
    const val API_STORIES = "stories"
    const val API_LOGIN = "login"
    const val API_PAGE = "page"
    const val API_SIZE = "size"
    const val API_LOCATION = "location"
    const val API_DESCRIPTION = "description"
    const val API_LAT = "lat"
    const val API_LON = "lon"
    const val STORY_TABLE = "story_table"
    const val DATABASE_NAME = "story_database"
    const val INITIAL_PAGE_INDEX = 1

    const val EXTRA_DATA = "data"

}