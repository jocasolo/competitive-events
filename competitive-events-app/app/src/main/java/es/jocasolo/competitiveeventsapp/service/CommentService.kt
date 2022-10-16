package es.jocasolo.competitiveeventsapp.service

import es.jocasolo.competitiveeventsapp.dto.comment.CommentDTO
import es.jocasolo.competitiveeventsapp.dto.comment.CommentPostDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPostDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPostDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface CommentService {

    @Headers("Content-Type: application/json")
    @POST("/comments")
    fun create(@Body body: CommentPostDTO, @Header(value="Authorization") authorization : String) : Call<CommentDTO>

}