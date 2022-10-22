package es.jocasolo.competitiveeventsapp.dto.punishment

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType

class PunishmentDTO(
        @SerializedName("id") var id: Int,
        @SerializedName("title") var title: String,
        @SerializedName("description") var description: String?,
        @SerializedName("looser") var looser: UserDTO?,
        @SerializedName("sortScore") var sortScore: ScoreSortType?,
        @SerializedName("requiredPosition") var requiredPosition: Int?,
        @SerializedName("image") var image: ImageDTO?
) {
}