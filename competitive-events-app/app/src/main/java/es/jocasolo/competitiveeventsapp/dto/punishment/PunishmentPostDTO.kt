package es.jocasolo.competitiveeventsapp.dto.punishment

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType

class PunishmentPostDTO(
        @SerializedName("title") var title: String,
        @SerializedName("description") var description: String? = null,
        @SerializedName("eventId") var eventId: String? = null,
        @SerializedName("sortScore") var sortScore: ScoreSortType? = null,
        @SerializedName("requiredPosition") var requiredPosition: Int? = null
) {
}