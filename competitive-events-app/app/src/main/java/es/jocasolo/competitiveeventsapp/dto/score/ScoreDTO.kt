package es.jocasolo.competitiveeventsapp.dto.score

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.image.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreStatusType
import java.util.*

class ScoreDTO(
    @SerializedName("id") var id: String,
    @SerializedName("value") var value: String?,
    @SerializedName("date") var date: Date?,
    @SerializedName("status") var status: ScoreStatusType?,
    @SerializedName("image") var image: ImageDTO?,
    @SerializedName("user") var user: UserDTO?
) : HistoryItemDTO() {
}