package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName

data class ImageDTO(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("url") var url: String
)