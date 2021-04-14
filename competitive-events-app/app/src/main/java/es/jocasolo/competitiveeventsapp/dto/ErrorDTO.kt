package es.jocasolo.competitiveeventsapp.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class ErrorDTO(
        @SerializedName("timestamp") var timestamp  : Date,
        @SerializedName("status") var status  : Int,
        @SerializedName("error") var error  : String,
        @SerializedName("trace") var trace  : String,
        @SerializedName("message") var message  : String,
        @SerializedName("path") var path  : String
)