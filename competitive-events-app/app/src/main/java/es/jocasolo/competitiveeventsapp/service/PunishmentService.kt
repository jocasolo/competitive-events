package es.jocasolo.competitiveeventsapp.service

import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentDTO
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentPostDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface PunishmentService {

    @Headers("Content-Type: application/json")
    @GET("/punishments/{id}")
    fun findOne(@Path (value="id") id: String, @Header(value="Authorization") authorization : String) : Call<PunishmentDTO>

    @Headers("Content-Type: application/json")
    @POST("/punishments")
    fun create(@Body body: PunishmentPostDTO, @Header(value="Authorization") authorization : String) : Call<PunishmentDTO>

    @Multipart
    @PUT("/punishments/{id}/image")
    fun updateImage(@Part file : MultipartBody.Part, @Path (value="id") id: Integer, @Header(value="Authorization") authorization : String) : Call<PunishmentDTO>

}