package com.candra.submissiononeintermediate.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.candra.submissiononeintermediate.room.dao.RemoteKeysDao
import com.candra.submissiononeintermediate.room.dao.StoryDao
import com.candra.submissiononeintermediate.room.entity.RemoteKeys
import com.candra.submissiononeintermediate.room.entity.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase() : RoomDatabase(){
    abstract fun storyDao(): StoryDao
    abstract fun remoteDao(): RemoteKeysDao
}