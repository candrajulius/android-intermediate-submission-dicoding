package com.candra.submissiononeintermediate.model.local

import android.os.Parcelable
import com.candra.submissiononeintermediate.model.response.StoryResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapStory(
    var id: String,
    var name: String,
    var createdAt: String,
    var photoUrl: String,
    var description: String,
    var lat: Double,
    var lon: Double
): Parcelable

fun List<StoryResponse>.toGenereteMapStory(): MutableList<MapStory>{
    val listStoryUser = mutableListOf<MapStory>()
    this.forEach{listStoryUser.add(it.toStoryMap())}
    return listStoryUser
}

fun StoryResponse.toStoryMap() = MapStory(
    id = this.id?: "Data is null",
    name = this.name?: "Data is null",
    createdAt = this.createdAt?: "Data is null",
    photoUrl = this.photoUrl?: "Data is null",
    description = this.description?: "Data is null",
    lat = this.lat?: 0.0,
    lon = this.lon?: 0.0
)