 package es.jocasolo.competitiveeventsapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserPutDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.singleton.UserInfo
import es.jocasolo.competitiveeventsapp.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*

 class ProfileUpdateFragment : Fragment() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)
    private var sdf : SimpleDateFormat? = null
    private var txtName : TextView? = null
    private var txtSurname : TextView? = null
    private var txtBirthDate : TextView? = null
    private var txtDescription : TextView? = null
    private var spinner : ProgressBar? = null

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
        txtBirthDate?.setOnClickListener(View.OnClickListener { showDatePicker() })
        txtDescription = view.findViewById(R.id.txt_update_description)
        spinner = view.findViewById(R.id.spn_update_user)

        // Load user info
        initFields(UserInfo.getInstance(requireContext()).getUserDTO())
        view.findViewById<Button>(R.id.btn_update_user_save).setOnClickListener { update(view) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDatePicker(){

        val calendar = Calendar.getInstance()
        if (txtBirthDate?.text != null){
            calendar.time = sdf?.parse(txtBirthDate?.text.toString())
        } else {
            calendar.time = Date()
        }

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth -> updateBirthDayDate(
            year,
            month,
            dayOfMonth
        ) }
        val dialog = DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    private fun updateBirthDayDate(year: Int, month: Int, dayOfMonth: Int) {
        txtBirthDate?.text = "$dayOfMonth-$month-$year"
    }

    private fun initFields(user: UserDTO?) {

        sdf = SimpleDateFormat(getString(R.string.sdf_date))

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
            txtBirthDate?.text = sdf?.format(user.birthDate!!)
        } ?: run {
            txtBirthDate?.visibility = View.INVISIBLE
        }
        user?.description?.let {
            txtDescription?.text = user.description
        } ?: run {
            txtDescription?.visibility = View.INVISIBLE
        }
    }

    private fun update(view: View){
        MyUtils.closeKeyboard(this.requireContext(), view)
        if(validate()) {

            val sdfApi : SimpleDateFormat = SimpleDateFormat(getString(R.string.sdf_api_date))
            val birthDate = sdf?.parse(txtBirthDate?.text.toString())

            spinner?.visibility = View.VISIBLE

            val actualUserDto = UserInfo.getInstance(requireContext()).getUserDTO();
            val updatedUserDto = UserPutDTO(
                null,
                txtName?.text.toString(),
                txtSurname?.text.toString(),
                txtDescription?.text.toString(),
                sdfApi.format(birthDate!!)
            )

            userService.update(
                actualUserDto?.id.toString(), updatedUserDto, UserAccount.getInstance(
                    requireContext()
                ).getToken()
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        actualUserDto?.name = updatedUserDto.name.toString();
                        actualUserDto?.surname = updatedUserDto.surname.toString();
                        showSuccessDialog(getString(R.string.success_updated_user))
                    } else {
                        try {
                            val errorDto = Gson().fromJson(
                                response.errorBody()?.string(),
                                ErrorDTO::class.java
                            ) as ErrorDTO
                            showErrorDialog(getString(Message.forCode(errorDto.message)))
                        } catch (e: Exception) {
                            showErrorDialog(getString(R.string.error_api_undefined))
                        }
                    }
                    spinner?.visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showErrorDialog(getString(R.string.error_api_undefined))
                    spinner?.visibility = View.INVISIBLE
                }
            })
        }
    }

    private fun validate() : Boolean{
        var result = true
        if(txtName?.text?.isEmpty()!!){
            result = false
            txtName?.error = getString(R.string.error_required)
        }
        if(txtSurname?.text?.isEmpty()!!){
            result = false
            txtSurname?.error = getString(R.string.error_required)
        }
        if(txtBirthDate?.text?.isEmpty()!!) {
            result = false
            txtBirthDate?.error = getString(R.string.error_required)
        }
        return result
    }

    private fun showSuccessDialog(message: String) {
        MyDialog.message(this, getString(R.string.success_action_title), message)
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}