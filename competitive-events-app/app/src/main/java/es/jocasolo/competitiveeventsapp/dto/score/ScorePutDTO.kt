package es.jocasolo.competitiveeventsapp.dto.score

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreStatusType
import java.util.*

class ScorePutDTO(
        @SerializedName("value") var value: String? = null,
        @SerializedName("status") var status: ScoreStatusType? = null,
        @SerializedName("eventId") var eventId: String? = null,
) : BackStackEntryDTO() {
}