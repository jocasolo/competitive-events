package es.jocasolo.competitiveeventsapp.service

import es.jocasolo.competitiveeventsapp.dto.login.LoginDTO
import es.jocasolo.competitiveeventsapp.dto.login.TokenDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserPostDTO
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(@Body body: LoginDTO) : Call<TokenDTO>

    @Headers("Content-Type: application/json")
    @GET("/users/{id}")
    fun findUser(@Path (value="id") id: String, @Header(value="Authorization") authorization : String) : Call<UserDTO>

    @HEAD("/users/{id}")
    fun exists(@Path (value="id") id: String) : Call<Void>

    @Headers("Content-Type: application/json")
    @POST("/users")
    fun create(@Body body: UserPostDTO) : Call<UserDTO>
}