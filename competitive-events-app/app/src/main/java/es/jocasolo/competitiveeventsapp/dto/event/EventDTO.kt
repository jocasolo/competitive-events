package es.jocasolo.competitiveeventsapp.dto.event

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.enums.event.EventVisibilityType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import java.io.Serializable
import java.util.*

class EventDTO(
        @SerializedName("id") var id: String,
        @SerializedName("title") var title: String,
        @SerializedName("subtitle") var subtitle: String,
        @SerializedName("description") var description: String,
        @SerializedName("type") var type: EventType?,
        @SerializedName("inscription") var inscription: EventInscriptionType?,
        @SerializedName("visibility") var visibility: EventVisibilityType?,
        @SerializedName("approvalNeeded") var approvalNeeded: Boolean?,
        @SerializedName("initDate") var initDate: Date?,
        @SerializedName("endDate") var endDate: Date?,
        @SerializedName("maxPlaces") var maxPlaces: Int?,
        @SerializedName("image") var image: ImageDTO?,
        @SerializedName("scoreType") var scoreType: ScoreValueType?,
        @SerializedName("sortScore") var sortScore: ScoreSortType?,
        @SerializedName("numParticipants") var numParticipants: Int?
) : Serializable {
}