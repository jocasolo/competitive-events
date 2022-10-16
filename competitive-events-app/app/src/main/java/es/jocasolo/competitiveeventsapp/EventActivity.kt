package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import es.jocasolo.competitiveeventsapp.ui.adapters.ViewPagerAdapter

class EventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        // Actionbar
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Get event id to show detail
        val extras = intent.extras
        var id : String? = null
        if(extras != null) {
            id = extras.getString("eventId")
        }

        // Page adapter for tabs
        val tabLayout = findViewById<TabLayout>(R.id.tab_event_main)
        val viewPager = findViewById<ViewPager>(R.id.viewpager_event_main)

        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, 3, id!!)
        viewPager?.adapter = pagerAdapter
        viewPager?.clearOnPageChangeListeners()
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout?.clearOnTabSelectedListeners()
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }

}