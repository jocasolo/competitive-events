package es.jocasolo.competitiveeventsapp.fragment.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.ui.adapters.ViewPagerAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventTabsFragment(var eventId: String? = null) : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var tabLayout : TabLayout? = null
    private var viewPager : ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Show event detail
        var id = arguments?.getString("eventId")
        if(id == null) id = eventId

        tabLayout = view.findViewById(R.id.tab_event_main)
        viewPager = view.findViewById(R.id.viewpager_event_main)

        val pagerAdapter = ViewPagerAdapter(parentFragmentManager, 3, id!!)
        viewPager?.adapter = pagerAdapter
        viewPager?.clearOnPageChangeListeners()
        viewPager?.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout?.clearOnTabSelectedListeners()
        tabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        viewPager?.currentItem = 0

        // Action bar title
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!

        //super.onViewCreated(view, savedInstanceState)

    }

}