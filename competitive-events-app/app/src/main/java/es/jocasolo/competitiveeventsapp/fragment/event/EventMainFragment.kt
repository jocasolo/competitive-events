package es.jocasolo.competitiveeventsapp.fragment.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.ui.adapters.ViewPagerAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventMainFragment(var eventId: String? = null) : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Show event detail
        var id = arguments?.getString("eventId")
        if(id == null) id = eventId

        super.onViewCreated(view, savedInstanceState)
    }


}