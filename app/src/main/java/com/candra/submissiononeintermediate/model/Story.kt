package com.candra.submissiononeintermediate.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    var id: String,
    var name: String,
    var createdAt: String,
    var photoUrl: String,
    var description: String
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
        description = this.description?: "Data is null"
)