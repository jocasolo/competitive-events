package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import java.time.LocalDateTime
import java.util.*

class UserLiteWithEventDTO(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("surname") var surname: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("birthDate") var birthDate: Date? = null,
    @SerializedName("registerDate") var registerDate: Date? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("avatar") var avatar: ImageDTO? = null,
    @SerializedName("incorporationDate") var incorporationDate: Date?,
    @SerializedName("lastStatusDate") var lastStatusDate: Date?,
    @SerializedName("privilege") var privilege: EventUserPrivilegeType?,
    @SerializedName("status") var status: EventUserStatusType?
) : HistoryItemDTO() {

}