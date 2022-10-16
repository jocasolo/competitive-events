package es.jocasolo.competitiveeventsapp.dto

import java.util.*

open class HistoryItemDTO(var historyType : HistoryItemType? = null) {
    enum class HistoryItemType {
        COMMENT_USER, COMMENT_OWN, SCORE;
    }

    var sortDate : Date? = null
}