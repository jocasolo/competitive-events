package es.jocasolo.competitiveeventsapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.image.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserLiteWithEventDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import java.io.IOException
import java.io.InputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*


object MyUtils {
    fun closeKeyboard(context: Context, view: View){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @Throws(IOException::class)
    fun getProperty(key: String?, context: Context): String? {
        val properties = Properties()
        val assetManager = context.assets
        val inputStream: InputStream = assetManager.open("config.properties")
        properties.load(inputStream)
        return properties.getProperty(key)
    }

    fun getTimeToFinish(context: Context, date: Date?): CharSequence {
        var result = ""
        date?.let {
            val end = Instant.ofEpochMilli(date.time)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            val now = LocalDateTime.now()
            val diff: Long = ChronoUnit.SECONDS.between(now, end)
            result = when {
                diff in 0..59 -> ChronoUnit.SECONDS.between(now, end).toString() + " " + context.getString(R.string.seconds)
                diff in 60..3599 -> ChronoUnit.MINUTES.between(now, end).toString() + " " + context.getString(R.string.minutes)
                diff in 3600..863999 -> ChronoUnit.HOURS.between(now, end).toString() + " " + context.getString(R.string.hours)
                diff > 864000 -> ChronoUnit.DAYS.between(now, end).toString() + " " + context.getString(R.string.days)
                else -> ""
            }
        }

        return result;
    }

    fun isAdmin(users: List<UserLiteWithEventDTO>?, username: String) : Boolean {
        users?.forEach {
            if(it.id.equals(username) && it.privilege == EventUserPrivilegeType.OWNER){
                return true
            }
        }
        return false
    }

    fun zoomToThisImage(context: Context, image: ImageDTO) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(image.link()), "image/*")
        context.startActivity(intent)
    }
}