package es.jocasolo.competitiveeventsapp.fragment.participant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.ParticipantDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScoreDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreStatusType
import es.jocasolo.competitiveeventsapp.fragment.event.EventListener
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ListParticipantsAdapter
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
class ParticipantsListFragment(var eventId: String) : Fragment(), EventListener {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var participantsRecyclerView: RecyclerView? = null
    private var participantsAdapter: ListParticipantsAdapter? = null

    override fun backStackAction(data: BackStackEntryDTO) {
        loadEvent(eventId)
    }

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

        // Show participants
        var id = arguments?.getString("eventId")
        if(id == null) id = eventId
        loadEvent(id)

        view.findViewById<Button>(R.id.btn_participant_invite).setOnClickListener {
            inviteParticipant(id)
        }

    }

    private fun inviteParticipant(eventId : String?) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        val prev = parentFragmentManager.findFragmentByTag("dialogParticipantsInvite")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newDialogFragment = ParticipantsInviteDialogFragment(this, eventId!!)
        newDialogFragment.show(ft, "dialogParticipantsInvite")
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
            participantsAdapter = ListParticipantsAdapter(
                requireContext(), this, users.toList(), eventId, event.scoreType!!,
                MyUtils.isAdmin(event.users, UserAccount.getInstance(requireContext()).getName()))
            participantsRecyclerView?.adapter = participantsAdapter
        }

    }

    private fun getSortedUsers(event: EventDTO): List<ParticipantDTO> {

        val participants = mutableListOf<ParticipantDTO>()
        event.users?.forEach {
            val participant = ParticipantDTO(
                it.id,
                it.avatar?.link(),
                it.privilege,
                it.status,
                getScore(event.scores, it.id!!))
                participants.add(participant)
        }
        return if(event.sortScore == ScoreSortType.ASC) {
            participants.sortedBy { p -> p.score }
        } else {
            participants.sortedByDescending { p -> p.score }
        }
    }

    private fun getScore(scores: List<ScoreDTO>?, userId: String): Double? {
        var maxScore : Double? = 0.0
        scores?.forEach {
            if(it.user?.id.equals(userId) && it.status == ScoreStatusType.VALID){
                maxScore = (maxScore!!).coerceAtLeast(it.value?.toDouble()!!)
                maxScore = (maxScore!!).coerceAtLeast(it.value?.toDouble()!!)
            }
        }
        return maxScore
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}