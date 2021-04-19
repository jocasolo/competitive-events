package es.jocasolo.competitiveeventsapp.utils

import android.content.res.Resources
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.jocasolo.competitiveeventsapp.R

object MyDialog {
    fun confirmNavigate(fragment : Fragment, title : String, message : String, navigateToId: Int){
        val activity = fragment.requireActivity()
        val builder: AlertDialog = activity.let {
            AlertDialog.Builder(it)
                    .setMessage(message)
                    .setTitle(title)
                    .setPositiveButton(activity.resources.getString(R.string.confirm)) { _, _ ->
                        fragment.findNavController().navigate(navigateToId)
                    }
                    .create()
        }
        builder.show()
    }

    fun confirmCancelNavigate(fragment : Fragment, title : String, message : String, navigateToId: Int){
        val activity = fragment.requireActivity()
        val builder: AlertDialog = activity.let {
            AlertDialog.Builder(it)
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(activity.resources.getString(R.string.confirm)) { _, _ ->
                    fragment.findNavController().navigate(navigateToId)
                }
                .setNegativeButton(Resources.getSystem().getString(R.string.cancel)) { _, _ ->
                }
                .create()
        }
        builder.show()
    }

    fun message (fragment : Fragment, title : String, message : String){
        val activity = fragment.requireActivity()
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