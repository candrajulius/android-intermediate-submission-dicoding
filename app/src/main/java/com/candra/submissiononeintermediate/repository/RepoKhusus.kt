package com.candra.submissiononeintermediate.repository

import com.candra.submissiononeintermediate.api.ApiInterface
import com.candra.submissiononeintermediate.helper.Contant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoKhusus @Inject constructor(
    private val khusus: ApiInterface
){
    suspend fun getStriesData(token: String,page: Int,size: Int,location: Int) = withContext(Dispatchers.IO){
        khusus.getAllStories(Contant.BARER + token,page,size,location)
    }

    suspend fun getStoryUserWithLocation(token: String,page: Int,size: Int,location: Int) = khusus.getAllStories(Contant.BARER + token,page,size,location)

}