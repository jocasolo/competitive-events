 package es.jocasolo.competitiveeventsapp.fragment.user

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserPutDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.singleton.UserInfo
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
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

        // Action bar title
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = getString(R.string.update_profile)
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sdf = SimpleDateFormat(getString(R.string.sdf_date))

        txtName = view.findViewById(R.id.txt_update_name)
        txtSurname = view.findViewById(R.id.txt_update_surname)
        txtBirthDate = view.findViewById(R.id.txt_update_birthdate)
        txtBirthDate?.setOnClickListener(View.OnClickListener { showDatePicker() })
        txtDescription = view.findViewById(R.id.txt_update_description)
        spinner = view.findViewById(R.id.spn_update_user)

        // Avatar button
        view.findViewById<Button>(R.id.btn_profile_update_avatar).setOnClickListener { imageChooser() }

        // Load user info
        initFields(UserInfo.getInstance(requireContext()).getUserDTO())
        view.findViewById<Button>(R.id.btn_update_user_save).setOnClickListener { update(view) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDatePicker(){

        val calendar = Calendar.getInstance()
        if (StringUtils.isNotEmpty(txtBirthDate?.text)){
            calendar.time = sdf?.parse(txtBirthDate?.text.toString())
        } else {
            calendar.time = Date()
        }

        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth -> updateBirthDayDate(
                year,
                month,
                dayOfMonth
        ) }
        val dialog = DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }

    private fun updateBirthDayDate(year: Int, month: Int, dayOfMonth: Int) {
        val monthAdd = month + 1
        txtBirthDate?.text = "$dayOfMonth-$monthAdd-$year"
    }

    private fun initFields(user: UserDTO?) {

        sdf = SimpleDateFormat(getString(R.string.sdf_date))

        user?.avatar?.let {
            Picasso.get().load(user.avatar!!.link()).into(view?.findViewById<ImageView>(R.id.img_update_avatar))
        }
        user?.name?.let {
            txtName?.text = user.name
        }
        user?.surname?.let {
            txtSurname?.text = user.surname
        }
        user?.birthDate?.let {
            txtBirthDate?.text = sdf?.format(user.birthDate!!)
        }
        user?.description?.let {
            txtDescription?.text = user.description
        }
    }

    private fun update(view: View){
        MyUtils.closeKeyboard(this.requireContext(), view)

        val sdfApi : SimpleDateFormat = SimpleDateFormat(getString(R.string.sdf_api_date))
        var birthDateText : Date? = null
        if(StringUtils.isNotEmpty(txtBirthDate?.text)){
            birthDateText = sdf?.parse(txtBirthDate?.text.toString())
        }
        var birthDate : String? = null
        if(birthDateText != null){
            birthDate = sdfApi.format(birthDateText)
        }

        spinner?.visibility = View.VISIBLE

        val actualUserDto = UserInfo.getInstance(requireContext()).getUserDTO();
        val updatedUserDto = UserPutDTO(
                null,
                txtName?.text.toString(),
                txtSurname?.text.toString(),
                txtDescription?.text.toString(),
                birthDate
        )

        userService.update(
                actualUserDto?.id.toString(), updatedUserDto, UserAccount.getInstance(
                requireContext()).getToken()
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    actualUserDto?.name = updatedUserDto.name.toString();
                    actualUserDto?.surname = updatedUserDto.surname.toString();
                    actualUserDto?.description = updatedUserDto.description.toString()
                    updatedUserDto.birthDate?.let {
                        actualUserDto?.birthDate = sdfApi.parse(updatedUserDto.birthDate)
                    }
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

     private fun imageChooser() {
         val intent = Intent(Intent.ACTION_PICK)
         intent.type = "image/*"
         startActivityForResult(intent, 200)
     }

     // Update avatar
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

         super.onActivityResult(requestCode, resultCode, data)

         if (resultCode == -1) {
             if (requestCode == 200) {
                 val selectedImageUri: Uri? = data?.data
                 if (null != selectedImageUri) {

                     spinner?.visibility = View.VISIBLE

                     val imageView = view?.findViewById<ImageView>(R.id.img_update_avatar)
                     imageView?.visibility = View.VISIBLE
                     Picasso.get().load(selectedImageUri).into(imageView)

                     // Upload image to storage service
                     val filePath: String = getImagePath(selectedImageUri)
                     val file = File(filePath)
                     val filePart = MultipartBody.Part.createFormData("file", file.name, RequestBody.create(MediaType.parse("image/*"), file));

                     userService.updateAvatar(filePart, UserAccount.getInstance(requireContext()).getName(), UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<UserDTO> {
                         override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                             if (response.code() == HttpURLConnection.HTTP_OK) {
                                 UserInfo.getInstance(requireContext()).getUserDTO()?.avatar = response.body()?.avatar;
                                 showSuccessDialog(getString(R.string.success_updated_avatar))
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
                         override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                             showErrorDialog(getString(R.string.error_api_undefined))
                             spinner?.visibility = View.INVISIBLE
                         }
                     })
                 }
             }
         }
     }

     private fun getImagePath(selectedImageUri: Uri): String {
         val filePath: String
         if ("content" == selectedImageUri.scheme) {
             val cursor = requireContext().contentResolver.query(selectedImageUri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null)
             cursor?.moveToFirst()
             filePath = cursor!!.getString(0)
             cursor.close()
         } else {
             filePath = selectedImageUri.path.toString()
         }
         return filePath
     }

     private fun showSuccessDialog(message: String) {
         MyDialog.message(this, getString(R.string.success_action_title), message)
     }

     private fun showErrorDialog(message: String) {
         MyDialog.message(this, getString(R.string.error_title), message)
     }
}