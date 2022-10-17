package es.jocasolo.competitiveeventsapp.fragment.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.ParticipantDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScoreDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ListParticipantsAdapter
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventParticipantsFragment(var eventId: String? = null) : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var participantsRecyclerView: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_participants, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        participantsRecyclerView = view.findViewById(R.id.recycler_event_participants)
        participantsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        // Show event detail
        var id = arguments?.getString("eventId")
        if(id == null) id = eventId
        if(id != null) {
            loadEvent(id)
        }

    }

    private fun loadEvent(id: String) {
        eventService.findOne(id, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventDTO> {
            override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val event = response.body()
                    if (event != null) {
                        showEventParticipants(event)
                    }
                } else {
                    try {
                        val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e: Exception) {
                        showErrorDialog(getString(R.string.error_api_undefined))
                    }
                }
            }

            override fun onFailure(call: Call<EventDTO>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
            }
        })
    }

    private fun showEventParticipants(event: EventDTO) {

        // Participants
        if(event.users != null) {
            val users = getSortedUsers(event)
            participantsRecyclerView?.adapter = ListParticipantsAdapter(requireContext(), users.toList())

        }

    }

    private fun getSortedUsers(event: EventDTO): List<ParticipantDTO> {

        val participants = mutableListOf<ParticipantDTO>()
        event.users?.forEach {
            val participant = ParticipantDTO(
                it.id,
                it.avatar?.link(),
                getRole(it.privilege),
                getScore(event.scores, it.id!!))
            participants.add(participant)
        }

        return participants.sortedBy { p -> p.score }
    }

    private fun getScore(scores: List<ScoreDTO>?, userId: String): Double? {
        var maxScore : Double? = 0.0
        scores?.forEach {
            if(it.user?.id.equals(userId)){
                maxScore = (maxScore!!).coerceAtLeast(it.value?.toDouble()!!)
            }
        }
        return maxScore
    }

    private fun getRole(privilege: EventUserPrivilegeType?): String {
        return when(privilege){
            EventUserPrivilegeType.OWNER -> getString(R.string.owner)
            EventUserPrivilegeType.USER -> getString(R.string.user)
            else -> getString(R.string.user)
        }
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}