package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName
import java.util.*

class UserPostDTO(
    @SerializedName("id") var username: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("name") var name: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("birthDate") var birthDate: Date? = null,
    @SerializedName("phone") var phone: String? = null
)