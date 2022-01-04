package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.singleton.UserInfo
import java.text.SimpleDateFormat

class ProfileUpdateFragment : Fragment() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)
    private var txtName : TextView? = null
    private var txtSurname : TextView? = null
    private var txtBirthDate : TextView? = null
    private var txtDescription : TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        txtName = view.findViewById(R.id.txt_update_name)
        txtSurname = view.findViewById(R.id.txt_update_surname)
        txtBirthDate = view.findViewById(R.id.txt_update_birthdate)
        txtDescription = view.findViewById(R.id.txt_update_description)

        // Load user info
        initFields(UserInfo.getInstance(requireContext()).getUserDTO())
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initFields(user : UserDTO?) {
        val sdf : SimpleDateFormat = SimpleDateFormat(getString(R.string.sdf_date))
        user?.avatar?.let {
            Picasso.get().load(user.avatar!!.url).into(view?.findViewById<ImageView>(R.id.img_update_avatar))
        }
        user?.name?.let {
            txtName?.text = user.name
        } ?: run {
            txtName?.visibility = View.INVISIBLE
        }
        user?.surname?.let {
            txtSurname?.text = user.surname
        } ?: run {
            txtSurname?.visibility = View.INVISIBLE
        }
        user?.birthDate?.let {
            txtBirthDate?.text = sdf.format(user.birthDate)
        } ?: run {
            txtBirthDate?.visibility = View.INVISIBLE
        }
        user?.description?.let {
            txtDescription?.text = user.description
        } ?: run {
            txtDescription?.visibility = View.INVISIBLE
        }
    }

}