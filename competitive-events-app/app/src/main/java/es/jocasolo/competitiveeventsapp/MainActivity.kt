package es.jocasolo.competitiveeventsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.jocasolo.competitiveeventsapp.dto.user.TokenDTO
import es.jocasolo.competitiveeventsapp.dto.user.request.LoginRequest
import es.jocasolo.competitiveeventsapp.services.ServiceBuilder
import es.jocasolo.competitiveeventsapp.services.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        login("test1", "Test123454")
    }

    private fun login(username: String, password: String){
        CoroutineScope(Dispatchers.IO).launch {
            val userService = ServiceBuilder.buildService(UserService::class.java)
            val call = userService.login(LoginRequest(username, password)).execute()
            if(call.isSuccessful){
                val token = call.body()
                println(token?.accessToken)
            } else {
                call.code()
                println(call.code())
            }
        }
    }

}