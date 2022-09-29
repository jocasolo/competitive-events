package es.jocasolo.competitiveeventsapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.IOException
import java.io.InputStream
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
}