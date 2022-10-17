package es.jocasolo.competitiveeventsapp.dto

import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import okhttp3.MultipartBody
import java.io.Serializable

/**
 * will extend from this class, the objects that are passed to the previous screen by means of arguments.
 * Adds to these objects an image that is included once the item has been created in the database.
 */
open class BackStackEntryDTO (
    var imagePart: MultipartBody.Part? = null
) : Serializable {
}