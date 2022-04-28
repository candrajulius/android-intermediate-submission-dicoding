package com.candra.submissiononeintermediate.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.candra.submissiononeintermediate.model.Story

@Database(
    entities = [Story::class,RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase() : RoomDatabase(){
    abstract fun storyDao(): StoryDao
    abstract fun remoteDao(): RemoteKeysDao
}