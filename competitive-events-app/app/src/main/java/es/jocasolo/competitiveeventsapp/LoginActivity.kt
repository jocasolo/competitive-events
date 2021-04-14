package es.jocasolo.competitiveeventsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.jocasolo.competitiveeventsapp.dto.login.LoginDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login("test1", "Test123454")
    }

    private fun login(username: String, password: String){
        CoroutineScope(Dispatchers.IO).launch {
            val userService = ServiceBuilder.buildService(UserService::class.java)
            val call = userService.login(LoginDTO(username, password)).execute()
            if(call.isSuccessful){
                val token = call.body()
                println(token?.accessToken)
            } else {
                println(call.code())
            }
        }
    }

}