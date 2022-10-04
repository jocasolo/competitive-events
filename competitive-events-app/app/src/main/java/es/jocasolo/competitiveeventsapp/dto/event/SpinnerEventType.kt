package es.jocasolo.competitiveeventsapp.dto.event

import es.jocasolo.competitiveeventsapp.enums.event.EventType

class SpinnerEventType(var key : EventType, var value : String) {

    override fun toString(): String {
        return value
    }

}