package es.jocasolo.competitiveeventsapp.dto.reward

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

class RewardDTO(
        @SerializedName("id") var id: String,
        @SerializedName("title") var title: String,
        @SerializedName("description") var description: String?,
        @SerializedName("winner") var winner: UserDTO?,
        @SerializedName("sortScore") var sortScore: ScoreSortType?,
        @SerializedName("requiredPosition") var requiredPosition: Int?,
        @SerializedName("image") var image: ImageDTO?
) {
}