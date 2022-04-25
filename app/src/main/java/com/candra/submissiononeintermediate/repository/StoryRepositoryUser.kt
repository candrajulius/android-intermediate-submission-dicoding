package com.candra.submissiononeintermediate.repository

import com.candra.submissiononeintermediate.api.ApiInterface
import com.candra.submissiononeintermediate.helper.Contant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepositoryUser @Inject constructor(
    private val retrofit: ApiInterface
)
{

    suspend fun postStory(token: String, file: MultipartBody.Part, description: RequestBody) =
        retrofit.addNewStrories(Contant.BARER + token,file,description)

    suspend fun getStory(token: String) = retrofit.getAllStories(Contant.BARER + token)
}