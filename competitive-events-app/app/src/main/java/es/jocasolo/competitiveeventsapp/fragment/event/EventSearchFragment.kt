package es.jocasolo.competitiveeventsapp.fragment.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventStatusType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ListEventAdapter
import es.jocasolo.competitiveeventsapp.ui.spinners.SpinnerEventType
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
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
    private var eventAdapter : ListEventAdapter? = null
    private var page = 0

    private var txtTitle : TextView? = null
    private var progressBar : ProgressBar? = null
    private var cmbEventType : Spinner? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events_search, container, false)
    }

    /**
     * Initialize all the fields on the fragment
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Action bar title
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = getString(R.string.events_search)

        // Init input fields
        txtTitle = view.findViewById(R.id.txt_event_search_title)
        progressBar = view.findViewById(R.id.spn_event_search)
        cmbEventType = view.findViewById(R.id.combo_events_search_type)

        // Combo box
        initEventTypeComboBox()

        // Event list
        initEventListRecyclerView()

        // Search button
        view.findViewById<Button>(R.id.btn_event_search).setOnClickListener {
            MyUtils.closeKeyboard(requireContext(), requireView())
            page = 0
            search()
        }
    }

    /**
     * List of events recycler view initialization
     */
    private fun initEventListRecyclerView() {
        // Event list
        if(eventAdapter == null){
            eventAdapter = ListEventAdapter(this, null)
        }
        recyclerView = requireView().findViewById(R.id.recycler_event_search)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = eventAdapter
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    search()
                }
            }
        }
        recyclerView?.addOnScrollListener(scrollListener)
    }

    /**
     * Comobobox for type of events initialization
     */
    private fun initEventTypeComboBox() {
        // Combo box event types
        val eventTypes: MutableList<SpinnerEventType> = ArrayList()
        eventTypes.add(SpinnerEventType(EventType.ALL, getString(R.string.events_all)))
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
    }

    /**
     * Search the events filtering by the fields that the user sets
     */
    private fun search() {

        val eventType = cmbEventType?.selectedItem as SpinnerEventType
        var title : String? = txtTitle?.text.toString()
        if(title != null && title.isEmpty()){
            title = null
        }
        var type : String? = eventType.key.name
        if(eventType.key == EventType.ALL){
            type = null
        }

        progressBar?.visibility = View.VISIBLE

        eventService.search(title, type, EventStatusType.ACTIVE.name, EventInscriptionType.PUBLIC.name,
                null,  page, 10, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventPageDTO> {
            override fun onResponse(call: Call<EventPageDTO>, response: Response<EventPageDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    response.body()?.events?.let {
                        if(page == 0) {
                            eventAdapter?.events = response.body()!!.events?.toMutableList()
                        } else {
                            eventAdapter?.addEvents(it)
                        }
                        eventAdapter?.notifyDataSetChanged()
                    }
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

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}