package es.jocasolo.competitiveeventsapp.dto.event

class SpinnerItem(var key : String, var value : String) {

    override fun toString(): String {
        return value
    }

}