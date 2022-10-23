package es.jocasolo.competitiveeventsapp.service

import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPostDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPutDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPostDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPutDTO
import okhttp3.MultipartBody
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
            @Query (value="status") status: String?,
            @Query (value="eventUserStatus") eventUserStatus: String?,
            @Query (value="inscription") inscription: String?,
            @Query (value="username") username: String?,
            @Query (value="page") page: Int?,
            @Query (value="size") size: Int?,
            @Header(value="Authorization") authorization : String) : Call<EventPageDTO>

    @Headers("Content-Type: application/json")
    @POST("/events")
    fun create(@Body body: EventPostDTO, @Header(value="Authorization") authorization : String) : Call<EventDTO>

    @Headers("Content-Type: application/json")
    @PUT("/events/{id}")
    fun update(@Path (value="id") id: String,@Body body: EventPutDTO, @Header(value="Authorization") authorization : String) : Call<Void>

    @Headers("Content-Type: application/json")
    @PUT("/events/{id}/init")
    fun init(@Path (value="id") id: String, @Header(value="Authorization") authorization : String) : Call<Void>

    @Headers("Content-Type: application/json")
    @PUT("/events/{id}/finish")
    fun finish(@Path (value="id") id: String, @Header(value="Authorization") authorization : String) : Call<Void>

    @Multipart
    @PUT("/events/{id}/image")
    fun updateImage(@Part file : MultipartBody.Part, @Path (value="id") id: String, @Header(value="Authorization") authorization : String) : Call<EventDTO>

    // USER EVENT

    @Headers("Content-Type: application/json")
    @GET("/events/{eventId}/users/{userId}")
    fun findEventAndUser(@Path (value="eventId") eventId: String, @Path (value="userId") userId: String, @Header(value="Authorization") authorization : String) : Call<EventUserDTO>

    @Headers("Content-Type: application/json")
    @POST("/events/{id}/users")
    fun addUser(@Path (value="id") id: String, @Body body: EventUserPostDTO, @Header(value="Authorization") authorization : String) : Call<EventUserDTO>

    @Headers("Content-Type: application/json")
    @PUT("/events/{id}/users")
    fun updateUser(@Path (value="id") id: String, @Body body: EventUserPutDTO, @Header(value="Authorization") authorization : String) : Call<Void>

}