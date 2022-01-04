package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*

data class UserDTO(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("surname") var surname: String,
    @SerializedName("description") var description: String,
    @SerializedName("birthDate") var birthDate: Date?,
    @SerializedName("registerDate") var registerDate: Date?,
    @SerializedName("email") var email: String?,
    @SerializedName("avatar") var avatar: ImageDTO?
)