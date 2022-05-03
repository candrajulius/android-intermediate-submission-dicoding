package com.candra.submissiononeintermediate.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.candra.submissiononeintermediate.helper.`object`.Contant
import com.candra.submissiononeintermediate.repository.RepoKhusus
import com.candra.submissiononeintermediate.room.entity.RemoteKeys
import com.candra.submissiononeintermediate.room.database.StoryDatabase
import com.candra.submissiononeintermediate.room.entity.Story
import com.candra.submissiononeintermediate.room.entity.toGenereteListStory
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject



@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(
    private val token: String,
    private val repoKhusus: RepoKhusus,
    private val storyDatabase: StoryDatabase,
): RemoteMediator<Int, Story>()
{

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosetToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: Contant.INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prefKey?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = repoKhusus.getStriesData(
                token = token,
                page = page,
                state.config.pageSize,
                0
            )

            val endOfPaginationReached = response.body()?.listStoryResponse.isNullOrEmpty()
            val data =  response.body()?.listStoryResponse?.toGenereteListStory()

            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    with(storyDatabase){
                        remoteDao().deleteAllRemoteKeys()
                        storyDao().deletAll()
                    }
                }
                val prefKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = data?.map {
                    RemoteKeys(id = it.id,prefKey = prefKey,nextKey = nextKey)
                }
                with(storyDatabase){
                    keys?.let { remoteDao().insertAll(it) }
                    data?.let { storyDao().insertStoryToDatabase(it) }
                }

            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (e: IOException){
            return MediatorResult.Error(e)
        }catch (e: HttpException){
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKeys?{
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            storyDatabase.remoteDao().getRemoteKeyId(it.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            storyDatabase.remoteDao().getRemoteKeyId(it.id)
        }
    }

    private suspend fun getRemoteKeyClosetToCurrentPosition(state: PagingState<Int, Story>): RemoteKeys? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let { id ->
                storyDatabase.remoteDao().getRemoteKeyId(id)
            }
        }
    }


}