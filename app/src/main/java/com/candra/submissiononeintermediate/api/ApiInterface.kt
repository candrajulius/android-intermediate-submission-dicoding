package com.candra.submissiononeintermediate.api

import com.candra.submissiononeintermediate.helper.Contant
import com.candra.submissiononeintermediate.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header(Contant.AUTHORIZATION) token: String,
    ): Response<AllStoryResponse>

    @Multipart
    @POST("stories")
    suspend fun addNewStrories(
        @Header(Contant.AUTHORIZATION) token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<AddNewStoryResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUsername(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

}