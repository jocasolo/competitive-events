package es.jocasolo.competitiveeventsapp.dto

import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType

class ParticipantDTO(
    var id: String? = null,
    var avatar: String? = null,
    var privilege: EventUserPrivilegeType? = null,
    var status: EventUserStatusType? = null,
    var score: Double? = null
) {
}