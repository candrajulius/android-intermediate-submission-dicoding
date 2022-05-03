package com.candra.submissiononeintermediate.api

import com.candra.submissiononeintermediate.helper.`object`.Contant
import com.candra.submissiononeintermediate.model.*
import com.candra.submissiononeintermediate.model.response.AddNewStoryResponse
import com.candra.submissiononeintermediate.model.response.AllStoryResponse
import com.candra.submissiononeintermediate.model.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST(Contant.API_REGISTER)
    suspend fun registerUser(
        @Field(Contant.API_NAME) name: String,
        @Field(Contant.API_EMAIL) email: String,
        @Field(Contant.API_PASSWORD) password: String
    ): Response<RegisterResponse>

    @GET(Contant.API_STORIES)
    suspend fun getAllStories(
        @Header(Contant.AUTHORIZATION) token: String,
        @Query(Contant.API_PAGE) page: Int,
        @Query(Contant.API_SIZE) size: Int,
        @Query(Contant.API_LOCATION) location: Int? = null
    ): Response<AllStoryResponse>

    @Multipart
    @POST(Contant.API_STORIES)
    suspend fun addNewStrories(
        @Header(Contant.AUTHORIZATION) token: String,
        @Part file: MultipartBody.Part,
        @Part(Contant.API_DESCRIPTION) description: RequestBody,
        @Part(Contant.API_LAT) lat: RequestBody?,
        @Part(Contant.API_LON) lon: RequestBody?
    ): Response<AddNewStoryResponse>

    @FormUrlEncoded
    @POST(Contant.API_LOGIN)
    suspend fun loginUsername(
        @Field(Contant.API_EMAIL) email: String,
        @Field(Contant.API_PASSWORD) password: String
    ): Response<LoginResponse>

}