package es.jocasolo.competitiveeventsapp

import android.accounts.AccountManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accManager: AccountManager = AccountManager.get(this)
        setContentView(R.layout.activity_login)
    }

}