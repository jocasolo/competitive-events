package es.jocasolo.competitiveeventsapp.dto.reward

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType

class RewardPostDTO(
        @SerializedName("title") var title: String,
        @SerializedName("description") var description: String? = null,
        @SerializedName("eventId") var eventId: String? = null,
        @SerializedName("sortScore") var sortScore: ScoreSortType? = null,
        @SerializedName("requiredPosition") var requiredPosition: Int? = null,
        @SerializedName("id") var id: Int? = null,
) : BackStackEntryDTO() {
}