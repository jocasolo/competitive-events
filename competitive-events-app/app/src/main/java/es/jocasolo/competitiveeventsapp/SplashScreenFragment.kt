package es.jocasolo.competitiveeventsapp

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
import es.jocasolo.competitiveeventsapp.constants.Constants
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.login.LoginDTO
import es.jocasolo.competitiveeventsapp.dto.login.TokenDTO
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

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SplashScreenFragment : Fragment() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val accManager = AccountManager.get(context)
        for(account in accManager.accounts){
            if(account.type.equals(Constants.ACCOUNT_TYPE)){
                login(account, accManager)
            }
        }
    }

    private fun login(account : Account, accManager : AccountManager) {
        val loginDto = LoginDTO(account.name, accManager.getPassword(account))
        userService.login(loginDto).enqueue(object : Callback<TokenDTO> {
            override fun onResponse(call: Call<TokenDTO>, response: Response<TokenDTO>) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    val token = response.body()
                    token?.let {
                        println("Logged")
                        accManager.setAuthToken(account, token.tokenType, token.accessToken)
                        accManager.setUserData(account, Constants.REFRESH_TOKEN, token.refreshToken)
                        accManager.setUserData(account, Constants.EXPIRES_IN, token.expiresIn)
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        requireActivity().finish()
                    }
                } else {
                    findNavController().navigate(R.id.action_Splash_to_Login)
                }
            }
            override fun onFailure(call: Call<TokenDTO>, t: Throwable) {
                findNavController().navigate(R.id.action_Splash_to_Login)
            }
        })
    }

}