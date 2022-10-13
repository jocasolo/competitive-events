package es.jocasolo.competitiveeventsapp.fragment.login

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.MainActivity
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.constants.Constants
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.login.LoginDTO
import es.jocasolo.competitiveeventsapp.dto.login.TokenDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class LoginFragment : Fragment() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)

    private var txtUsername : TextView? = null
    private var txtPassword : TextView? = null
    private var spinner : ProgressBar? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init input fields
        txtUsername = view.findViewById(R.id.txt_login_username)
        txtPassword = view.findViewById(R.id.txt_login_password)
        spinner = view.findViewById(R.id.spn_login)

        // Events
        view.findViewById<Button>(R.id.btn_login).setOnClickListener { login(view) }
        view.findViewById<Button>(R.id.btn_login_register).setOnClickListener {
            findNavController().navigate(R.id.action_Login_to_Register)
        }
    }

    private fun login(view : View){
        MyUtils.closeKeyboard(this.requireContext(), view)
        if(validate()) {
            spinner?.visibility = View.VISIBLE

            val loginDto = LoginDTO(txtUsername?.text.toString(), txtPassword?.text.toString())
            userService.login(loginDto).enqueue(object : Callback<TokenDTO> {
                override fun onResponse(call: Call<TokenDTO>, response: Response<TokenDTO>) {
                    if(response.code() == HttpURLConnection.HTTP_OK) {
                        val token = response.body()
                        token?.let {
                            val accManager: AccountManager = AccountManager.get(context)
                            val account = Account(txtUsername?.text.toString(), "competitiveevents.com")
                            accManager.addAccountExplicitly(account, txtPassword?.text.toString(), null)
                            accManager.setUserData(account, Constants.ACCESS_TOKEN, token.accessToken)
                            accManager.setUserData(account, Constants.REFRESH_TOKEN, token.refreshToken)
                            accManager.setUserData(account, Constants.EXPIRES_IN, token.expiresIn)
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        }
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
        var result = true
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