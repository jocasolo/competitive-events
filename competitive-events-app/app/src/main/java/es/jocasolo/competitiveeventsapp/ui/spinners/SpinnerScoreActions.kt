package es.jocasolo.competitiveeventsapp.ui.spinners

import es.jocasolo.competitiveeventsapp.enums.actions.ScoreActions

class SpinnerScoreActions(var key : ScoreActions, var value : String) {

    override fun toString(): String {
        return value
    }

}