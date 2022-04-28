package com.candra.submissiononeintermediate.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.candra.submissiononeintermediate.api.ApiInterface
import com.candra.submissiononeintermediate.helper.Contant
import com.candra.submissiononeintermediate.model.Story
import com.candra.submissiononeintermediate.paging.StoryRemoteMediator
import com.candra.submissiononeintermediate.room.StoryDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepositoryUser @Inject constructor(
    private val khusus: RepoKhusus,
    private val retrofit: ApiInterface,
    private val database: StoryDatabase
)
{

    suspend fun postStory(
        token: String, file: MultipartBody.Part, description: RequestBody,
        latitude: RequestBody? = null,
        longtitude: RequestBody? = null) =
        retrofit.addNewStrories(Contant.BARER + token,file,description,latitude,longtitude)

    @OptIn(ExperimentalPagingApi::class)
    fun getStory(token: String): LiveData<PagingData<Story>>{
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(token,khusus,database),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

}