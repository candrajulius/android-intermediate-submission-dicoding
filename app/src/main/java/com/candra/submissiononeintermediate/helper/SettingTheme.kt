package com.candra.submissiononeintermediate.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.candra.submissiononeintermediate.helper.Contant.EMAIL
import com.candra.submissiononeintermediate.helper.Contant.ID
import com.candra.submissiononeintermediate.helper.Contant.LOGGIN
import com.candra.submissiononeintermediate.helper.Contant.NAME
import com.candra.submissiononeintermediate.helper.Contant.PASSWORD
import com.candra.submissiononeintermediate.helper.Contant.TOKEN
import com.candra.submissiononeintermediate.model.LoginUpUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SettingTheme
{
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")


    // Ambil data user yang telah diketik oleh user
    fun getDataUser(context: Context): Flow<LoginUpUser>{
        return context.dataStore.data.map { preferences ->
            LoginUpUser(
                id = preferences[ID],
                password = preferences[PASSWORD],
                email = preferences[EMAIL],
                token = preferences[TOKEN],
                isLogginIn = preferences[LOGGIN],
                name = preferences[NAME]
            )
        }
    }

    suspend fun updateDataUser(context: Context,user: LoginUpUser) = context.dataStore.edit { preferences ->
        user.id?.let { preferences[ID] = it }
        user.password?.let { preferences[PASSWORD] = it }
        user.email?.let { preferences[EMAIL] = it }
        user.token?.let { preferences[TOKEN] = it }
        user.isLogginIn?.let { preferences[LOGGIN] = it }
        user.name?.let { preferences[NAME] = it }
    }

}