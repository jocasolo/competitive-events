package es.jocasolo.competitiveeventsapp.dto.score

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreStatusType
import java.util.*

class ScorePostDTO(
        @SerializedName("value") var value: String? = null,
        @SerializedName("eventId") var eventId: String? = null,
) : BackStackEntryDTO() {
}