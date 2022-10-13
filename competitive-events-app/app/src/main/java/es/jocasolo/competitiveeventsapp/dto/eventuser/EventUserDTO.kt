package es.jocasolo.competitiveeventsapp.dto.eventuser

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.enums.event.EventVisibilityType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import java.util.*

class EventUserDTO(
        @SerializedName("userId") var userId: String,
        @SerializedName("eventId") var eventId: String? = null,
        @SerializedName("incorporationDate") var incorporationDate: Date? = null,
        @SerializedName("lastStatusDate") var lastStatusDate: Date? = null,
        @SerializedName("privilege") var privilege: EventUserPrivilegeType? = null,
        @SerializedName("status") var status: EventUserStatusType? = null,
) {

}