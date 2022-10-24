package es.jocasolo.competitiveeventsapp.dto

import java.util.*

open class HistoryItemDTO(var historyType : HistoryItemType? = null) {
    enum class HistoryItemType {
        COMMENT_USER, COMMENT_OWN, SCORE, USER_JOIN, WINNER, LOOSER, INIT_EVENT, END_EVENT;
    }

    var sortDate : Date? = null
}