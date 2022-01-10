package es.jocasolo.competitiveeventsapp.dto.event

import com.google.gson.annotations.SerializedName

class EventPageDTO(
        @SerializedName("total") var total : Long? = null,
        @SerializedName("pages") var pages: Int? = null,
        @SerializedName("hasNext") var hasNext: Boolean? = null,
        @SerializedName("hasPrevious") var hasPrevious: Boolean? = null,
        @SerializedName("events") var events: List<EventDTO>? = null
)