package es.jocasolo.competitiveeventsapp

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserPostDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    private val usernamePattern = Pattern.compile("^(?=.*[a-z])(?=\\S+$).{4,16}$")
    private val userService = ServiceBuilder.buildService(UserService::class.java)

    private var txtUsername : TextView? = null
    private var txtEmail : TextView? = null
    private var txtPassword : TextView? = null
    private var txtPasswordConfirm : TextView? = null

    private var validUsername = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Init input fields
        txtUsername = findViewById(R.id.txt_username)
        txtEmail = findViewById(R.id.txt_email)
        txtPassword = findViewById(R.id.txt_password)
        txtPasswordConfirm = findViewById(R.id.txt_password_confirm)

        txtUsername?.text = "test1"
        txtEmail?.text = "jocasolo.test@gmail.com"
        txtPassword?.text = "Test123456"
        txtPasswordConfirm?.text = "Test123456"

        // Events
        findViewById<Button>(R.id.btn_register).setOnClickListener { register() }
        findViewById<TextView>(R.id.txt_username).setOnFocusChangeListener { _, _ -> usernameExists() }
    }

    private fun register() {
        txtUsername?.onEditorAction(EditorInfo.IME_ACTION_DONE)
        MyUtils.closeKeyboard(baseContext, txtUsername!!)
        if(validate() && validUsername) {
            val userDTO = UserPostDTO(txtUsername?.text.toString(), txtEmail?.text.toString(), txtPassword?.text.toString())
            userService.create(userDTO).enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if(response.code() == HttpURLConnection.HTTP_CREATED){
                        showSuccessDialog()

                    } else {
                        val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                        errorDto?.let {
                            Toast.makeText(applicationContext, getString(Message.forCode(errorDto.message)), Toast.LENGTH_LONG).show()
                        } .run {
                            Toast.makeText(applicationContext, getString(R.string.error_api_undefined), Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Toast.makeText(applicationContext, getString(R.string.error_api_undefined), Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun showSuccessDialog() {
        val builder: AlertDialog = this.let {
            AlertDialog.Builder(it)
                    .setMessage(R.string.user_created)
                    .setTitle(R.string.user_created_title)
                    .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                    }
                    .create()
        }
        builder.show()
    }

    private fun validate() : Boolean {
        val validUsername = validateUsername()
        val validEmail = validateEmail()
        val validPassword = validatePassword()
        return validUsername && validEmail && validPassword
    }

    private fun usernameExists() {
        validUsername = false
        if(txtUsername?.text?.isNotEmpty()!!) {
            userService.exists(txtUsername?.text.toString()).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.code() == HttpURLConnection.HTTP_OK) {
                        txtUsername?.error = getString(R.string.error_username_exists)
                        validUsername = false
                    } else {
                        validUsername = true
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    validUsername = true
                    Toast.makeText(applicationContext, getString(R.string.error_api_undefined), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun validateUsername() : Boolean {
        var result = true

        if(txtUsername?.text?.isEmpty()!!){
            txtUsername?.error = getString(R.string.error_required)
            result = false

        } else if(!usernamePattern.matcher(txtUsername?.text.toString()).matches()) {
            txtUsername?.error = getString(R.string.error_username_not_valid)
            result = false
        }

        return result
    }


    private fun validateEmail() : Boolean {
        var result = true

        if(txtEmail?.text?.isEmpty()!!){
            txtEmail?.error = getString(R.string.error_required)
            result = false
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(txtEmail?.text.toString()).matches()) {
            txtEmail?.error = getString(R.string.error_email_not_valid)
            result = false
        }

        return result
    }

    private fun validatePassword() : Boolean {
        var result = true

        if(txtPassword?.text?.isEmpty()!!){
            txtPassword?.error = getString(R.string.error_required)
            result = false
        } else if(!passwordPattern.matcher(txtPassword?.text.toString()).matches()) {
            txtPassword?.error = getString(R.string.error_password_not_valid)
            result = false
        }
        if(txtPasswordConfirm?.text?.isEmpty()!!){
            txtPasswordConfirm?.error = getString(R.string.error_required)
            result = false
        }

        return result
    }
}