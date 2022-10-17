package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import java.time.LocalDateTime
import java.util.*

class UserLiteWithEventDTO(
    @SerializedName("incorporationDate") var incorporationDate: Date?,
    @SerializedName("lastStatusDate") var lastStatusDate: Date?,
    @SerializedName("privilege") var privilege: EventUserPrivilegeType?,
    @SerializedName("status") var status: EventUserStatusType?
) : UserDTO(){

}