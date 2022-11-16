package es.jocasolo.competitiveeventsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.ui.adapters.ListEventAdapter
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class HomeFragment : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var eventRecyclerView: RecyclerView? = null
    private var eventAdapter : ListEventAdapter? = null
    private var eventInvitedRecyclerView: RecyclerView? = null
    private var eventInvitedAdapter : ListEventAdapter? = null

    private var page = 0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Action bar title
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = getString(R.string.your_events)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.txt_events_empty_search).setOnClickListener {
            findNavController().navigate(R.id.action_home_to_event_new)
        }

        initUserEventsAccepted()
        initUserEventsInvited()

        loadUserEvents(EventUserStatusType.ACCEPTED.name, eventAdapter!!, true)
        loadUserEvents(EventUserStatusType.INVITED.name, eventInvitedAdapter!!, false)

    }

    override fun onResume() {
        super.onResume()
        if(eventAdapter != null) {
            loadUserEvents(EventUserStatusType.ACCEPTED.name, eventAdapter!!, true)
        }
    }

    private fun initUserEventsAccepted() {
        // Events list view
        if(eventAdapter == null){
            eventAdapter = ListEventAdapter(this, null, ListEventAdapter.ListEventType.HOME)
        }
        eventRecyclerView = view?.findViewById(R.id.recycler_event_list)
        eventRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        eventRecyclerView?.adapter = eventAdapter
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    page++
                    loadUserEvents(EventUserStatusType.ACCEPTED.name, eventAdapter!!, true)
                }
            }
        }
        eventRecyclerView?.addOnScrollListener(scrollListener)
    }

    private fun initUserEventsInvited() {
        // Events list view
        eventInvitedAdapter = ListEventAdapter(this, null, ListEventAdapter.ListEventType.INVITATIONS)
        eventInvitedRecyclerView = view?.findViewById(R.id.recycler_event_list_invited)
        eventInvitedRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        eventInvitedRecyclerView?.adapter = eventInvitedAdapter
    }

    private fun loadUserEvents(eventUserStatus: String?, adapter: ListEventAdapter, paginate: Boolean) {

        val p = if(paginate) page else null
        val size = if(paginate) 10 else null

        eventService.search(
            null,
            null,
            null,
            eventUserStatus,
            null,
            UserAccount.getInstance(requireContext()).getName(),
            p,
            size,
            UserAccount.getInstance(requireContext()).getToken()
        ).enqueue(object : Callback<EventPageDTO> {
            override fun onResponse(call: Call<EventPageDTO>, response: Response<EventPageDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    response.body()?.events?.let {
                        if (page == 0) {
                            adapter.events = response.body()!!.events?.toMutableList()
                        } else {
                            adapter.addEvents(it)
                        }
                        adapter.notifyDataSetChanged()
                        if(adapter.events != null && adapter.events?.isEmpty() == false){
                            requireView().findViewById<TextView>(R.id.txt_events_empty).visibility = View.GONE
                            requireView().findViewById<TextView>(R.id.txt_events_empty_search).visibility = View.GONE
                        }
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
            }

            override fun onFailure(call: Call<EventPageDTO>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
            }
        })
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}