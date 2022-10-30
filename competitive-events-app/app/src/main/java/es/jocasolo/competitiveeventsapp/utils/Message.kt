package es.jocasolo.competitiveeventsapp.utils

import es.jocasolo.competitiveeventsapp.R

object Message {

    const val ERROR_EMAIL_EXISTS = "error.email.exists"
    const val ERROR_NOT_AVAILABLE_PLACES = "error.event.not-available-places"

    fun forCode(code : String) : Int {
        return when(code){
            ERROR_EMAIL_EXISTS -> R.string.error_email_exists
            ERROR_NOT_AVAILABLE_PLACES -> R.string.error_event_not_available_places
            else -> R.string.error_api_undefined
        }
    }
}