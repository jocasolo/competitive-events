package es.jocasolo.competitiveeventsapp

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.login.LoginDTO
import es.jocasolo.competitiveeventsapp.dto.login.TokenDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class LoginActivity : AppCompatActivity() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)

    private var txtUsername : TextView? = null
    private var txtPassword : TextView? = null
    private var spinner : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Init input fields
        txtUsername = findViewById(R.id.txt_login_username)
        txtPassword = findViewById(R.id.txt_login_password)
        spinner = findViewById(R.id.spn_login)

        // Events
        findViewById<Button>(R.id.btn_login_register).setOnClickListener { showRegisterActivity() }
        findViewById<Button>(R.id.btn_login).setOnClickListener { login() }
    }

    private fun showRegisterActivity() {
        startActivity(Intent(baseContext, RegisterActivity::class.java))
    }

    private fun login(){
        MyUtils.closeKeyboard(baseContext, this.findViewById(android.R.id.content))
        if(validate()) {
            spinner?.visibility = View.VISIBLE

            val loginDto = LoginDTO(txtUsername?.text.toString(), txtPassword?.text.toString())
            userService.login(loginDto).enqueue(object : Callback<TokenDTO> {
                override fun onResponse(call: Call<TokenDTO>, response: Response<TokenDTO>) {
                    if(response.code() == HttpURLConnection.HTTP_OK) {
                        val token = response.body()
                        println(token?.refreshToken)
                    } else {
                        try {
                            val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                            showErrorDialog(getString(Message.forCode(errorDto.message)))
                        } catch (e : Exception) {
                            showErrorDialog(getString(R.string.error_api_undefined))
                        }
                    }
                    spinner?.visibility = View.INVISIBLE
                }
                override fun onFailure(call: Call<TokenDTO>, t: Throwable) {
                    showErrorDialog(getString(R.string.error_api_undefined))
                    spinner?.visibility = View.INVISIBLE
                }
            })
        }
    }

    private fun validate() : Boolean{
        var result = true;
        if(txtUsername?.text?.isEmpty()!!){
            result = false
            txtUsername?.error = getString(R.string.error_required)
        }
        if(txtPassword?.text?.isEmpty()!!){
            result = false
            txtPassword?.error = getString(R.string.error_required)
        }
        return result
    }

    private fun showErrorDialog(message : String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}