package com.candra.submissiononeintermediate.helper.di

import android.content.Context
import androidx.room.Room
import com.candra.submissiononeintermediate.helper.`object`.Contant.DATABASE_NAME
import com.candra.submissiononeintermediate.room.database.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        StoryDatabase::class.java,
        DATABASE_NAME
    ).build()
}