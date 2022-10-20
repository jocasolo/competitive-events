package es.jocasolo.competitiveeventsapp.service

import es.jocasolo.competitiveeventsapp.dto.comment.CommentDTO
import es.jocasolo.competitiveeventsapp.dto.comment.CommentPostDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPostDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPostDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPutDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScoreDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScorePostDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScorePutDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ScoreService {

    @Headers("Content-Type: application/json")
    @POST("/scores")
    fun create(@Body body: ScorePostDTO, @Header(value="Authorization") authorization : String) : Call<ScoreDTO>

    @Multipart
    @PUT("/scores/{id}/image")
    fun updateImage(@Part file : MultipartBody.Part, @Path (value="id") id: Int, @Header(value="Authorization") authorization : String) : Call<ScoreDTO>

    @Headers("Content-Type: application/json")
    @PUT("/scores/{id}")
    fun update(@Path (value="id") id: String, @Body body: ScorePutDTO, @Header(value="Authorization") authorization : String) : Call<Void>

}