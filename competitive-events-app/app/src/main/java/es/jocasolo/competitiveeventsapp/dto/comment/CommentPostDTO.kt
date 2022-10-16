package es.jocasolo.competitiveeventsapp.dto.comment

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import java.util.*

class CommentPostDTO(
        @SerializedName("text") var text: String,
        @SerializedName("eventId") var eventId: String
) : HistoryItemDTO() {
}