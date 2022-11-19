package es.jocasolo.competitiveeventsapp.dto.eventuser

import com.google.gson.annotations.SerializedName

class EventUserPostDTO(
        @SerializedName("username") var username: String,
        @SerializedName("reject") var reject: Boolean? = null,
        @SerializedName("phone") var phone: String? = null,
        @SerializedName("email") var email: String? = null
) {

}