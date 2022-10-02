package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.adapter.CustomAdapter
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.service.UserService
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class HomeFragment : Fragment() {

    private val userService = ServiceBuilder.buildService(UserService::class.java)
    private val eventService = ServiceBuilder.buildService(EventService::class.java)
    private var eventsPage = EventPageDTO()
    private var recyclerView: RecyclerView? = null

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

        // Events list view
        loadUserEvents()
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_event_list)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun loadUserEvents() {
        eventService.search(null, null, null, null, null, 0, 10, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventPageDTO> {
            override fun onResponse(call: Call<EventPageDTO>, response: Response<EventPageDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    recyclerView?.adapter = CustomAdapter(response.body()!!)
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
                //spinner?.visibility = View.INVISIBLE
            }
            override fun onFailure(call: Call<EventPageDTO>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
                //spinner?.visibility = View.INVISIBLE
            }
        })
    }

    private fun showSuccessDialog(message: String) {
        MyDialog.message(this, getString(R.string.success_action_title), message)
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}