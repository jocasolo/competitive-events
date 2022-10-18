package es.jocasolo.competitiveeventsapp.dto.event

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.comment.CommentDTO
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScoreDTO
import es.jocasolo.competitiveeventsapp.dto.user.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserLiteWithEventDTO
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
        @SerializedName("numParticipants") var numParticipants: Int?,
        @SerializedName("rewards") var rewards: List<RewardDTO>?,
        @SerializedName("punishments") var punishments: List<PunishmentDTO>?,
        @SerializedName("comments") var comments: List<CommentDTO>?,
        @SerializedName("scores") var scores: List<ScoreDTO>?,
        @SerializedName("users") var users: List<UserLiteWithEventDTO>?,
) {
}