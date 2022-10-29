package es.jocasolo.competitiveeventsapp.dto.event

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.enums.event.EventVisibilityType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType

class EventPostDTO(
        @SerializedName("title") var title: String,
        @SerializedName("scoreType") var scoreType: ScoreValueType? = null,
        @SerializedName("sortScore") var sortScore: ScoreSortType? = null,
        @SerializedName("type") var type: EventType? = null,
        @SerializedName("inscription") var inscription: EventInscriptionType? = null,
        @SerializedName("visibility") var visibility: EventVisibilityType? = null,
        @SerializedName("approvalNeeded") var approvalNeeded: Boolean? = null,
        @SerializedName("subtitle") var subtitle: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("initDate") var initDate: String? = null,
        @SerializedName("endDate") var endDate: String? = null,
        @SerializedName("maxPlaces") var maxPlaces: Int? = null
) {

}