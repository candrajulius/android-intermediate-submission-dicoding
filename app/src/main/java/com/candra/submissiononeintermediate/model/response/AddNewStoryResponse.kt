package com.candra.submissiononeintermediate.model.response


import com.google.gson.annotations.SerializedName

data class AddNewStoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)