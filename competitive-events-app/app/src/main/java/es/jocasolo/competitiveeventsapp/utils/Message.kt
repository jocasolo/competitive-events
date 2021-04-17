package es.jocasolo.competitiveeventsapp.utils

import es.jocasolo.competitiveeventsapp.R

object Message {

    const val ERROR_EMAIL_EXISTS = "error.email.exists"

    fun forCode(code : String) : Int {
        return when(code){
            ERROR_EMAIL_EXISTS -> R.string.error_email_exists
            else -> R.string.error_api_undefined
        }
    }
}