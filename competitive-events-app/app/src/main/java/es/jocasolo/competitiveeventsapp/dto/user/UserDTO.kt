package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.image.ImageDTO
import java.util.*

open class UserDTO(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("birthDate") var birthDate: Date? = null,
    @SerializedName("registerDate") var registerDate: Date? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("avatar") var avatar: ImageDTO? = null
)