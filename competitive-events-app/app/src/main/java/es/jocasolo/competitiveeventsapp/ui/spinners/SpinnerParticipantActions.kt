package es.jocasolo.competitiveeventsapp.ui.spinners

import es.jocasolo.competitiveeventsapp.enums.actions.ParticipantActions

class SpinnerParticipantActions(var key : ParticipantActions, var value : String) {

    override fun toString(): String {
        return value
    }

}