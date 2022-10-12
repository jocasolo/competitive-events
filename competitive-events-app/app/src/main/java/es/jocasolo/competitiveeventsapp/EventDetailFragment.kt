package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import android.transition.Visibility
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserPostDTO
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventDetailFragment : Fragment() {

    private var txtTitle : TextView? = null
    private var txtSubtitle : TextView? = null
    private var txtDescription : TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Init input fields
        txtTitle = view.findViewById(R.id.txt_event_detail_title)
        txtSubtitle = view.findViewById(R.id.txt_event_detail_subtitle)
        txtDescription = view.findViewById(R.id.txt_event_detail_description)

        // Show event detail
        val event : EventDTO = arguments?.getSerializable("event") as EventDTO
        showEventDetail(event)

        super.onViewCreated(view, savedInstanceState)
    }

    private fun showEventDetail(event: EventDTO) {

        // Action bar title
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = event.title

        // Event texts
        txtTitle?.text = event.title
        if(StringUtils.isNotEmpty(event.subtitle)){
            txtSubtitle?.text = event.subtitle
        } else {
            txtSubtitle?.visibility = View.GONE
        }
        System.out.println(event.description)
        System.out.println(txtDescription?.text)
        if(StringUtils.isNotEmpty(event.description)){
            txtDescription?.text = event.description
        } else {
            txtDescription?.visibility = View.GONE
        }

    }

}