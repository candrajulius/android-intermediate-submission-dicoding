package com.candra.submissiononeintermediate.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.candra.submissiononeintermediate.room.entity.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryToDatabase(story: List<Story>)

    @Query("SELECT * FROM story_table")
    fun getAllStory(): PagingSource<Int,Story>

    @Query("DELETE FROM story_table")
    suspend fun deletAll()
}