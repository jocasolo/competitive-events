package es.jocasolo.competitiveeventsapp.ui.spinners

import es.jocasolo.competitiveeventsapp.enums.event.EventType

class SpinnerEventType(var key : EventType, var value : String) {

    override fun toString(): String {
        return value
    }

}