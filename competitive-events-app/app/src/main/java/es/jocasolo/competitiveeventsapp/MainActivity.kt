package es.jocasolo.competitiveeventsapp

import android.accounts.Account
import android.accounts.AccountManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.constants.Constants
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.login.LoginDTO
import es.jocasolo.competitiveeventsapp.dto.login.TokenDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.utils.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class MainActivity : AppCompatActivity() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accManager: AccountManager = AccountManager.get(this)
        setContentView(R.layout.activity_login)

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
                    }
                } else {

                }
            }
            override fun onFailure(call: Call<TokenDTO>, t: Throwable) {

            }
        })
    }

}