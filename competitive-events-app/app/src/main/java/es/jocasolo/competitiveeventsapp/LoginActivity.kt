package es.jocasolo.competitiveeventsapp

import android.accounts.AccountManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CompetitiveEventsApp_NoActionBar)
        setContentView(R.layout.activity_login)
    }

}