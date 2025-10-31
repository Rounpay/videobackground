package com.example

import com.example.myapplication.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("files/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Call<FileUploadResponse>
}