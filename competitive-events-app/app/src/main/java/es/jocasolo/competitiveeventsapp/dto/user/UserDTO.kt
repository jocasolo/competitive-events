package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("surname") var surname: String,
    @SerializedName("description") var description: String
)