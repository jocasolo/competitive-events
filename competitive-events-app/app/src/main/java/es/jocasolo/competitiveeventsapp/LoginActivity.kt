package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CompetitiveEventsApp_NoActionBar)
        setContentView(R.layout.activity_login)
    }

    override fun onBackPressed() {
        finish()
    }

}