package es.jocasolo.competitiveeventsapp.utils

import es.jocasolo.competitiveeventsapp.R

object Message {

    const val ERROR_EMAIL_EXISTS = "error.email.exists"
    const val ERROR_PHONE_EXISTS = "error.phone.exists"
    const val ERROR_NOT_AVAILABLE_PLACES = "error.event.not-available-places"
    const val USER_NOT_FOUND = "error.user.not-found"

    fun forCode(code : String) : Int {
        return when(code){
            ERROR_EMAIL_EXISTS -> R.string.error_email_exists
            ERROR_NOT_AVAILABLE_PLACES -> R.string.error_event_not_available_places
            USER_NOT_FOUND -> R.string.error_user_not_found
            else -> R.string.error_api_undefined
        }
    }
}