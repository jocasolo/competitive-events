package es.jocasolo.competitiveeventsapp.fragment.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.singleton.UserInfo
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.text.SimpleDateFormat


class ProfileFragment(private val userId: String? = null) : DialogFragment() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)

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

        // Load user info
        if(userId == null) {
            // Navigate to profile update
            view.findViewById<Button>(R.id.btn_profile_modify).setOnClickListener {
                findNavController().navigate(R.id.action_profile_to_update_profile)
            }
            // Init own user profile
            initFields(UserInfo.getInstance(requireContext()).getUserDTO())

        } else {
            view.findViewById<Button>(R.id.btn_profile_modify)?.text = getString(R.string.back)
            view.findViewById<Button>(R.id.btn_profile_modify)?.setOnClickListener { dismiss() }
            txtEmail?.visibility = View.GONE
            findUser(userId)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun findUser(userId: String) {
        userService.findUser(userId, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    initFields(response.body())
                }
            }
            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
            }
        })
    }

    private fun initFields(user: UserDTO?) {
        val sdf : SimpleDateFormat = SimpleDateFormat(getString(R.string.sdf_date))
        txtUsername?.text = user?.id
        txtEmail?.text = user?.email
        user?.avatar?.let {
            Picasso.get().load(user.avatar!!.link()).into(view?.findViewById(R.id.img_profile_avatar))
            view?.findViewById<ImageView>(R.id.img_profile_avatar)?.setOnClickListener {
                MyUtils.zoomToThisImage(requireContext(), user.avatar!!)
            }
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