package es.jocasolo.competitiveeventsapp.dto.eventuser

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.enums.event.EventVisibilityType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import java.util.*

class EventUserPostDTO(
        @SerializedName("username") var username: String,
        @SerializedName("reject") var reject: Boolean? = null
) {

}