package es.jocasolo.competitiveeventsapp.fragment

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import es.jocasolo.competitiveeventsapp.MainActivity
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.constants.Constants
import es.jocasolo.competitiveeventsapp.dto.login.LoginDTO
import es.jocasolo.competitiveeventsapp.dto.login.TokenDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

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

        var hasAccount = false
        // User authorization
        val accManager = AccountManager.get(context)
        for(account in accManager.accounts){
            if(account.type.equals(Constants.ACCOUNT_TYPE)){
                hasAccount = true
                login(account, accManager)
            }
        }

        if(!hasAccount)
            findNavController().navigate(R.id.action_Splash_to_Login)
    }

    private fun login(account : Account, accManager : AccountManager) {
        val loginDto = LoginDTO(account.name, accManager.getPassword(account))
        userService.login(loginDto).enqueue(object : Callback<TokenDTO> {
            override fun onResponse(call: Call<TokenDTO>, response: Response<TokenDTO>) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    val token = response.body()
                    token?.let {
                        accManager.setUserData(account, Constants.ACCESS_TOKEN, token.accessToken)
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