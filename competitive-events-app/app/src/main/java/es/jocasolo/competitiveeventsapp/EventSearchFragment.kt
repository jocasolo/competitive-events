package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventStatusType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ListEventAdapter
import es.jocasolo.competitiveeventsapp.ui.spinners.SpinnerEventType
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventSearchFragment : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)
    private var recyclerView: RecyclerView? = null

    private var txtTitle : TextView? = null
    private var progressBar : ProgressBar? = null
    private var cmbEventType : Spinner? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_event_search)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        // Init input fields
        txtTitle = view.findViewById(R.id.txt_event_search_title)
        progressBar = view.findViewById(R.id.spn_event_search)
        cmbEventType = view.findViewById(R.id.combo_events_search_type)

        // Combo box event types
        val eventTypes: MutableList<SpinnerEventType> = ArrayList()
        eventTypes.add(SpinnerEventType(EventType.SPORTS, getString(R.string.events_sports)))
        eventTypes.add(SpinnerEventType(EventType.OTHER, getString(R.string.events_other)))
        eventTypes.add(SpinnerEventType(EventType.ACADEMIC, getString(R.string.events_academic)))
        eventTypes.add(SpinnerEventType(EventType.FAMILY, getString(R.string.events_family)))
        eventTypes.add(SpinnerEventType(EventType.VIDEOGAMES, getString(R.string.events_videogames)))

        ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                eventTypes
        ).also {  adapter ->
            cmbEventType?.adapter = adapter
        }
        cmbEventType?.setSelection(0)

        view.findViewById<Button>(R.id.btn_event_search).setOnClickListener { search(view) }

    }

    private fun search(view: View) {

        val eventType = cmbEventType?.selectedItem as SpinnerEventType
        var title : String? = txtTitle?.text.toString()
        if(title != null && title?.isEmpty()){
            title = null;
        }
        val type = eventType.key.name

        progressBar?.visibility = View.VISIBLE

        eventService.search(title, type, EventStatusType.ACTIVE.name, null,
                null, 0, 10, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventPageDTO> {
            override fun onResponse(call: Call<EventPageDTO>, response: Response<EventPageDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    recyclerView?.adapter = ListEventAdapter(findNavController(), response.body()!!, ListEventAdapter.ListEventType.SEARCH)
                } else {
                    try {
                        val errorDto = Gson().fromJson(
                                response.errorBody()?.string(),
                                ErrorDTO::class.java
                        ) as ErrorDTO
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e: Exception) {
                        showErrorDialog(getString(R.string.error_api_undefined))
                    }
                }
                progressBar?.visibility = View.INVISIBLE
            }
            override fun onFailure(call: Call<EventPageDTO>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
                progressBar?.visibility = View.INVISIBLE
            }
        })

    }

    private fun showSuccessDialog() {
        MyDialog.confirmNavigate(this, getString(R.string.user_created_title), getString(R.string.user_created),
                R.id.action_event_creation_to_home
        )
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}