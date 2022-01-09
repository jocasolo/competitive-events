package es.jocasolo.competitiveeventsapp.service

import es.jocasolo.competitiveeventsapp.dto.image.ImageDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ImageService {

    @Multipart
    @POST("/images")
    fun upload(@Part file : MultipartBody.Part, @Part(value="type") type : RequestBody, @Header(value="Authorization") authorization : String) : Call<ImageDTO>

}