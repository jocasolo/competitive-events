package es.jocasolo.competitiveeventsapp.dto

import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import okhttp3.MultipartBody
import java.io.Serializable

class RewardPunishmentDataDTO (
    var title: String,
    var description: String? = null,
    var requiredPosition: Int? = null,
    var sortScore: ScoreSortType? = null,
    var imagePart: MultipartBody.Part? = null

) : Serializable {
}