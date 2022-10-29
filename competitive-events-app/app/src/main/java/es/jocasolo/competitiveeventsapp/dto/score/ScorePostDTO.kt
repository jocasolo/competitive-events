package es.jocasolo.competitiveeventsapp.dto.score

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO

class ScorePostDTO(
        @SerializedName("value") var value: String? = null,
        @SerializedName("eventId") var eventId: String? = null,
) : BackStackEntryDTO() {
}