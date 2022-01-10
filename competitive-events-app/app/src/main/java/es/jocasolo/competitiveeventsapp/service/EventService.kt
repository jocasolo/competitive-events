package es.jocasolo.competitiveeventsapp.service

import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.dto.image.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface EventService {

    @Headers("Content-Type: application/json")
    @GET("/events/{id}")
    fun findOne(@Path (value="id") id: String, @Header(value="Authorization") authorization : String) : Call<EventDTO>

    @Headers("Content-Type: application/json")
    @GET("/events")
    fun search(
            @Query (value="title") title: String?,
            @Query (value="type") type: String?,
            @Query (value="title") status: String?,
            @Query (value="inscription") inscription: String?,
            @Query (value="username") username: String?,
            @Query (value="page") page: Int?,
            @Query (value="size") size: Int?,
            @Header(value="Authorization") authorization : String) : Call<EventPageDTO>

}