package es.jocasolo.competitiveeventsapp.fragment.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import es.jocasolo.competitiveeventsapp.R

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventNewFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Action bar title
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = getString(R.string.events)
        actionBar.setHomeButtonEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Navigate to event creation
        view.findViewById<CardView>(R.id.card_create_event).setOnClickListener {
            findNavController().navigate(R.id.action_event_new_to_event_creation)
        }

        // Navigate to event search
        view.findViewById<CardView>(R.id.card_search_event).setOnClickListener {
            findNavController().navigate(R.id.action_new_to_search)
        }

        super.onViewCreated(view, savedInstanceState)
    }

}