package es.jocasolo.competitiveeventsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserPostDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
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
    private var spinner : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Init input fields
        txtUsername = findViewById(R.id.txt_username)
        txtEmail = findViewById(R.id.txt_email)
        txtPassword = findViewById(R.id.txt_password)
        txtPasswordConfirm = findViewById(R.id.txt_password_confirm)
        spinner = findViewById(R.id.spn_register)

        // Events
        findViewById<Button>(R.id.btn_register).setOnClickListener { register() }
        findViewById<Button>(R.id.btn_back).setOnClickListener { finish() }
        findViewById<TextView>(R.id.txt_username).setOnFocusChangeListener { _, _ -> validateUsernameExistsAndCommit(false) }
    }

    private fun register() {
        MyUtils.closeKeyboard(baseContext, this.findViewById(android.R.id.content))
        if(validate()) {
            spinner?.visibility = View.VISIBLE
            validateUsernameExistsAndCommit(true)
        }
    }

    private fun validateUsernameExistsAndCommit(commitForm : Boolean) {
        if(txtUsername?.text?.isNotEmpty()!!) {
            userService.exists(txtUsername?.text.toString()).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.code() == HttpURLConnection.HTTP_OK) {
                        txtUsername?.error = getString(R.string.error_username_exists)
                        spinner?.visibility = View.INVISIBLE
                    } else if(commitForm) {
                        // Username doesn't exists
                        commit()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showErrorDialog(getString(R.string.error_api_undefined))
                    spinner?.visibility = View.INVISIBLE
                }
            })
        }
    }

    private fun commit() {
        val userDTO = UserPostDTO(txtUsername?.text.toString(), txtEmail?.text.toString(), txtPassword?.text.toString())
        userService.create(userDTO).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if(response.code() == HttpURLConnection.HTTP_CREATED){
                    showSuccessDialog()
                } else {
                    try {
                        val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                        if(errorDto.message == Message.ERROR_EMAIL_EXISTS){
                            txtEmail?.error = getString(R.string.error_email_exists)
                        }
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e : Exception) {
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

    private fun showSuccessDialog() {
        MyDialog.confirmActivity(this, getString(R.string.user_created_title), getString(R.string.user_created), LoginActivity::class.java)
    }

    private fun showErrorDialog(message : String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

    /**
     * Validate all the fields of the form.
     * @return True if all fields are correct and false if not
     */
    private fun validate() : Boolean {
        val validUsername = validateUsername()
        val validEmail = validateEmail()
        val validPassword = validatePassword()
        return validUsername && validEmail && validPassword
    }

    /**
     * Check that the username has a value and with valid characters.
     * @return True if the username is valid and false if not
     */
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

    /**
     * Check that the email has a value and with valid email format.
     * @return True if the email is valid and false if not
     */
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

    /**
     * Check that the password has a value and with valid password format.
     * @return True if the password is valid and false if not
     */
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