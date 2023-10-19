package edu.du.week6restapi

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SkiResortService {
    @POST("SkiResorts")
    suspend fun createResort(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("SkiResorts")
    suspend fun getResorts(): Response<ResponseBody>

    @GET("SkiResorts/{id}")
    suspend fun getResort(@Path("id") id: String): Response<ResponseBody>

    @PUT("SkiResorts/{id}")
    suspend fun updateResort(@Path("id") id: String, @Body requestBody: RequestBody): Response<ResponseBody>

    @DELETE("SkiResorts/{id}")
    suspend fun deleteResort(@Path("id") id: String): Response<ResponseBody>
}