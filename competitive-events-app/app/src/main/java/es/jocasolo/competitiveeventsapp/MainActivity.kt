package es.jocasolo.competitiveeventsapp

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.jocasolo.competitiveeventsapp.constants.Constants

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.setDisplayShowHomeEnabled(true);
        //supportActionBar?.setLogo(R.drawable.basketball);
        //supportActionBar?.setDisplayUseLogoEnabled(true);
        //supportActionBar?.setDisplayShowTitleEnabled(false)
        //supportActionBar?.title = " " + getString(R.string.your_events)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_main_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.homeFragment, R.id.profileFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.item_exit -> {
                finish()
                true
            }
            R.id.item_logout -> {
                logout()
                true
            }
            R.id.item_user_profile -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/

    private fun logout() {
        val accManager: AccountManager = AccountManager.get(baseContext)
        for(account in accManager.accounts){
            if(account.type.equals(Constants.ACCOUNT_TYPE)){
                accManager.removeAccountExplicitly(account)
                break
            }
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}