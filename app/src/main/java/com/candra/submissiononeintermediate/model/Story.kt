package com.candra.submissiononeintermediate.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.candra.submissiononeintermediate.helper.Contant
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(Contant.STORY_TABLE)
data class Story(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "createdAt")
    var createdAt: String,
    @ColumnInfo(name = "photoUrl")
    var photoUrl: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "lat")
    var lat: Double,
    @ColumnInfo(name = "lon")
    var lon: Double
): Parcelable

fun List<StoryResponse>.toGenereteListStory(): MutableList<Story>{
    val listStoryUser = mutableListOf<Story>()
    this.forEach{listStoryUser.add(it.toStoryUser())}
    return listStoryUser
}

fun StoryResponse.toStoryUser() = Story(
        id = this.id?: "Data is null",
        name = this.name?: "Data is null",
        createdAt = this.createdAt?: "Data is null",
        photoUrl = this.photoUrl?: "Data is null",
        description = this.description?: "Data is null",
        lat = this.lat?: 0.0,
        lon = this.lon?: 0.0
)