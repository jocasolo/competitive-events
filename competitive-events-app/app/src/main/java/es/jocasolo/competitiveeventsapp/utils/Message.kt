package es.jocasolo.competitiveeventsapp.utils

import es.jocasolo.competitiveeventsapp.R

object Message {
    fun forCode(code : String) : Int {
        return when(code){
            "error.email.exists" -> R.string.error_email_exists
            else -> R.string.error_api_undefined
        }
    }
}