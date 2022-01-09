package es.jocasolo.competitiveeventsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.singleton.UserInfo
import java.text.SimpleDateFormat


class ProfileFragment : Fragment() {

    private var txtUsername : TextView? = null
    private var txtEmail : TextView? = null
    private var txtName : TextView? = null
    private var txtSurname : TextView? = null
    private var txtBirthDate : TextView? = null
    private var txtRegisterDate : TextView? = null
    private var txtDescription : TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Action bar title
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = getString(R.string.profile)
        actionBar.setHomeButtonEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        txtName = view.findViewById(R.id.txt_profile_name)
        txtSurname = view.findViewById(R.id.txt_profile_surname)
        txtEmail = view.findViewById(R.id.txt_profile_email)
        txtUsername = view.findViewById(R.id.txt_profile_username)
        txtBirthDate = view.findViewById(R.id.txt_profile_birthDate)
        txtRegisterDate = view.findViewById(R.id.txt_profile_registerDate)
        txtDescription = view.findViewById(R.id.txt_profile_description)

        // Navigate to profile update
        view.findViewById<Button>(R.id.btn_profile_modify).setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_update_profile)
        }

        // Load user info
        initFields(UserInfo.getInstance(requireContext()).getUserDTO())

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initFields(user: UserDTO?) {
        val sdf : SimpleDateFormat = SimpleDateFormat(getString(R.string.sdf_date))
        txtUsername?.text = user?.id
        txtEmail?.text = user?.email
        user?.avatar?.let {
            Picasso.get().load(user.avatar!!.url).into(view?.findViewById<ImageView>(R.id.img_profile_avatar))
        }
        user?.name?.let {
            txtName?.text = user.name
            txtName?.visibility = View.VISIBLE
        }
        user?.surname?.let {
            txtSurname?.text = user.surname
            txtSurname?.visibility = View.VISIBLE
        }
        user?.registerDate?.let {
            txtRegisterDate?.text = sdf.format(user.registerDate!!)
            txtRegisterDate?.visibility = View.VISIBLE
        }
        user?.birthDate?.let {
            txtBirthDate?.text = sdf.format(user.birthDate!!)
            txtBirthDate?.visibility = View.VISIBLE
        }
        user?.description?.let {
            txtDescription?.text = user.description
            txtDescription?.visibility = View.VISIBLE
        }
    }

}