package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserPutDTO(
    @SerializedName("email") var email : String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("birthDate") var birthDate: String? = null
)