package es.jocasolo.competitiveeventsapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import es.jocasolo.competitiveeventsapp.fragment.event.EventDetailFragment
import es.jocasolo.competitiveeventsapp.fragment.event.EventMainFragment
import es.jocasolo.competitiveeventsapp.fragment.event.EventParticipantsFragment

class ViewPagerAdapter(
    fm: FragmentManager,
    private val totalTabs: Int,
    private val eventId : String) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                EventMainFragment(eventId)
            }
            1 -> {
                EventDetailFragment(eventId)
            }
            2 -> {
                EventParticipantsFragment(eventId)
            }
            else -> EventMainFragment(eventId)
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

}