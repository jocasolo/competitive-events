package es.jocasolo.competitiveeventsapp.dto.eventuser

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType

class EventUserPutDTO(
        @SerializedName("username") var username: String? = null,
        @SerializedName("status") var status: EventUserStatusType? = null,
        @SerializedName("privilege") var privilege: EventUserPrivilegeType? = null
) : BackStackEntryDTO() {

}