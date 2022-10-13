package es.jocasolo.competitiveeventsapp.ui.spinners

import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType

class SpinnerScoreType(var key : ScoreValueType, var value : String) {

    override fun toString(): String {
        return value
    }

}