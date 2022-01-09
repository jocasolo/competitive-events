package es.jocasolo.competitiveeventsapp.dto.image

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*

data class ImageDTO(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("url") var url: String
)