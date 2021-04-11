package es.jocasolo.competitiveeventsapp.services

import es.jocasolo.competitiveeventsapp.dto.user.request.LoginRequest
import es.jocasolo.competitiveeventsapp.dto.user.TokenDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(@Body body: LoginRequest) : Call<TokenDTO>
}