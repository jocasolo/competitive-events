package es.jocasolo.competitiveeventsapp.dto.comment

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import java.util.*

class CommentDTO(
        @SerializedName("id") var id: String,
        @SerializedName("text") var text: String?,
        @SerializedName("date") var date: Date?,
        @SerializedName("user") var user: UserDTO?
) : HistoryItemDTO() {
}