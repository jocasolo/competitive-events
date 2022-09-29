package es.jocasolo.competitiveeventsapp.dto.user

import com.google.gson.annotations.SerializedName
import es.jocasolo.competitiveeventsapp.BuildConfig

data class ImageDTO(
    @SerializedName("id") var id: String,
    @SerializedName("storageId") var storageId: String,
    @SerializedName("name") var name: String,
    @SerializedName("url") var url: String
) {
    fun link() : String{
        return BuildConfig.IMAGES_URL + url;
    }
}