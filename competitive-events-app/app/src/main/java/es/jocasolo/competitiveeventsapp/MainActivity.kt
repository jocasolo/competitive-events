package es.jocasolo.competitiveeventsapp

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.constants.Constants
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.singleton.UserInfo
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class MainActivity : AppCompatActivity() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigation between fragments with the bottom icons
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_main_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.homeFragment, R.id.profileFragment, R.id.profileUpdateFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Check storage and contact permissions
        checkPermission()

        // Asynchronous call for user info
        loadUser()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
            android.R.id.home -> {
                // Actionbar back button
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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

    private fun loadUser() {
        userService.findUser(
            UserAccount.getInstance(applicationContext).getName(), UserAccount.getInstance(
                applicationContext
            ).getToken()
        ).enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK -> {
                        UserInfo.getInstance(applicationContext).setUserDTO(response.body())
                    }
                    HttpURLConnection.HTTP_FORBIDDEN -> {
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    }
                    else -> {
                        try {
                            val errorDto = Gson().fromJson(
                                response.errorBody()?.string(),
                                ErrorDTO::class.java
                            ) as ErrorDTO
                            MyDialog.message(
                                this@MainActivity, getString(R.string.error_title), getString(
                                    Message.forCode(
                                        errorDto.message
                                    )
                                )
                            )
                        } catch (e: Exception) {
                            MyDialog.message(
                                this@MainActivity, getString(R.string.error_title), getString(
                                    R.string.error_api_undefined
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                MyDialog.message(
                    this@MainActivity, getString(R.string.error_title), getString(
                        R.string.error_api_undefined
                    )
                )
            }
        })
    }

    fun changeActionBarTitle(newTitle: String){
        supportActionBar?.title = newTitle
    }

    // Function to check and request permission.
    private fun checkPermission() {
        val permissions: MutableList<String> = mutableListOf()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            permissions.add(Manifest.permission.READ_CONTACTS)
        }
        if(permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 50)
        }
    }

}