package es.jocasolo.competitiveeventsapp.dto.eventuser

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import java.util.*

class EventUserDTO(
        @SerializedName("userId") var userId: String,
        @SerializedName("eventId") var eventId: String? = null,
        @SerializedName("incorporationDate") var incorporationDate: Date? = null,
        @SerializedName("lastStatusDate") var lastStatusDate: Date? = null,
        @SerializedName("privilege") var privilege: EventUserPrivilegeType? = null,
        @SerializedName("status") var status: EventUserStatusType? = null,
) : BackStackEntryDTO() {

}