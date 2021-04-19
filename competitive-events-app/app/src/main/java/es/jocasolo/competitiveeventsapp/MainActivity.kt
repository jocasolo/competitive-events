package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.setDisplayShowHomeEnabled(true);
        //supportActionBar?.setLogo(R.drawable.basketball);
        //supportActionBar?.setDisplayUseLogoEnabled(true);
        //supportActionBar?.setDisplayShowTitleEnabled(false)
        //supportActionBar?.title = " " + getString(R.string.your_events)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

}