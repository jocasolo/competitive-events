package es.jocasolo.competitiveeventsapp.service

import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardPostDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RewardService {

    @Headers("Content-Type: application/json")
    @GET("/rewards/{id}")
    fun findOne(@Path (value="id") id: String, @Header(value="Authorization") authorization : String) : Call<RewardDTO>

    @Headers("Content-Type: application/json")
    @POST("/rewards")
    fun create(@Body body: RewardPostDTO, @Header(value="Authorization") authorization : String) : Call<RewardDTO>

    @Headers("Content-Type: application/json")
    @DELETE("/rewards/{id}")
    fun delete(@Path (value="id") id: Int, @Header(value="Authorization") authorization : String) : Call<Void>

    @Multipart
    @PUT("/rewards/{id}/image")
    fun updateImage(@Part file : MultipartBody.Part, @Path (value="id") id: Int, @Header(value="Authorization") authorization : String) : Call<RewardDTO>

}