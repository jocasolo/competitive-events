package es.jocasolo.competitiveeventsapp.utils

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import es.jocasolo.competitiveeventsapp.LoginActivity
import es.jocasolo.competitiveeventsapp.R
import kotlin.coroutines.coroutineContext

object MyDialog {
    fun<T> confirmActivity(activity : Activity, title : String, message : String, clazz: Class<T>){
        val builder: AlertDialog = activity.let {
            AlertDialog.Builder(it)
                    .setMessage(R.string.user_created)
                    .setTitle(R.string.user_created_title)
                    .setPositiveButton(activity.resources.getString(R.string.confirm)) { _, _ ->
                        activity.startActivity(Intent(activity.baseContext, clazz))
                    }
                    .create()
        }
        builder.show()
    }

    fun<T> confirmCancelActivity(activity : Activity, title : String, message : String, clazz: Class<T>){
        val builder: AlertDialog = activity.let {
            AlertDialog.Builder(it)
                    .setMessage(message)
                    .setTitle(title)
                    .setPositiveButton(activity.resources.getString(R.string.confirm)) { _, _ ->
                        activity.startActivity(Intent(activity.baseContext, clazz))
                    }
                    .setNegativeButton(Resources.getSystem().getString(R.string.cancel)) { _, _ ->

                    }
                    .create()
        }
        builder.show()
    }

    fun message (activity : Activity, title : String, message : String){
        val builder: AlertDialog = activity.let {
            AlertDialog.Builder(it)
                    .setMessage(message)
                    .setTitle(title)
                    .setPositiveButton(activity.resources.getString(R.string.confirm)) { _, _ ->
                    }
                    .create()
        }
        builder.show()
    }
}