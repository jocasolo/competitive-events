package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName

data class TokenDTO(
    @SerializedName("access_token") var accessToken: String,
    @SerializedName("token_type") var tokenType: String,
    @SerializedName("expires_in") var expiresIn: String,
    @SerializedName("refresh_token") var refreshToken: String
)