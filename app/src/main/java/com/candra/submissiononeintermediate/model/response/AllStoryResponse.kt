package com.candra.submissiononeintermediate.model.response


import com.google.gson.annotations.SerializedName

data class AllStoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStoryResponse: List<StoryResponse>,
    @SerializedName("message")
    val message: String
)