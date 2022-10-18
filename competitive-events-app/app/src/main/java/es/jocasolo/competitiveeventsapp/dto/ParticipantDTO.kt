package es.jocasolo.competitiveeventsapp.dto

import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType

class ParticipantDTO(
    var name: String? = null,
    var image: String? = null,
    var privilege: EventUserPrivilegeType? = null,
    var score: Double? = null
) {
}