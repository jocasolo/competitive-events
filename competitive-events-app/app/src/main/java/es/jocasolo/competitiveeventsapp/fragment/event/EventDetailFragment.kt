package es.jocasolo.competitiveeventsapp.fragment.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.EventActivity
import es.jocasolo.competitiveeventsapp.MainActivity
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserDTO
import es.jocasolo.competitiveeventsapp.dto.eventuser.EventUserPostDTO
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserLiteWithEventDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventInscriptionType
import es.jocasolo.competitiveeventsapp.enums.event.EventType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ListPunishmentAdapter
import es.jocasolo.competitiveeventsapp.ui.adapters.ListRewardAdapter
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventDetailFragment(var eventId: String? = null) : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var showRejectButton : Boolean? = false

    private var rewardsRecyclerView: RecyclerView? = null
    private var punishmentsRecyclerView: RecyclerView? = null

    private var txtTitle : TextView? = null
    private var txtSubtitle : TextView? = null
    private var txtDescription : ReadMoreTextView? = null
    private var txtInitDate : TextView? = null
    private var txtEndDate : TextView? = null
    private var txtNumParticipants : TextView? = null
    private var txtAvailablePlaces : TextView? = null
    private var txtEventType : TextView? = null
    private var txtEventJoined : TextView? = null
    private var txtIdentifier : TextView? = null
    private var imgMain : ImageView? = null
    private var btnJoin : Button? = null
    private var btnReject : Button? = null
    private var progressBar : ProgressBar? = null
    private var scrollView : NestedScrollView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        rewardsRecyclerView = view.findViewById(R.id.recycler_reward_list)
        rewardsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        punishmentsRecyclerView = view.findViewById(R.id.recycler_punishment_list)
        punishmentsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        // Init input fields
        txtTitle = view.findViewById(R.id.txt_event_detail_title)
        txtSubtitle = view.findViewById(R.id.txt_event_detail_subtitle)
        txtDescription = view.findViewById(R.id.txt_event_detail_description)
        txtInitDate = view.findViewById(R.id.txt_event_detail_init_date)
        txtEndDate = view.findViewById(R.id.txt_event_detail_end_date)
        txtNumParticipants = view.findViewById(R.id.txt_event_detail_num_participants)
        txtAvailablePlaces = view.findViewById(R.id.txt_event_detail_available_places)
        txtEventType = view.findViewById(R.id.txt_event_detail_type)
        txtEventJoined = view.findViewById(R.id.txt_event_detail_user_joined)
        txtIdentifier = view.findViewById(R.id.txt_event_detail_id)
        imgMain = view.findViewById(R.id.img_event_detail_main)
        btnJoin = view.findViewById(R.id.btn_event_detail_join)
        btnReject = view.findViewById(R.id.btn_event_detail_reject)
        progressBar = view.findViewById(R.id.spn_event_detail)
        scrollView = view.findViewById(R.id.scrollview_events_detail)

        // Show event detail
        var id = arguments?.getString("eventId")
        if(id == null) id = eventId
        if(id != null) {
            loadEvent(id)
        }

        showRejectButton = arguments?.getBoolean("showRejectButton")
        if(showRejectButton == null){
            showRejectButton = false
        }

    }

    private fun loadEvent(id: String) {
        eventService.findOne(id, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventDTO> {
            override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val event = response.body()
                    if (event != null) {
                        showEventDetail(event)
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

    private fun showEventDetail(event: EventDTO) {

        val sdf : SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

        // Action bar title
        if(requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).changeActionBarTitle(event.title)
        }
        if(requireActivity() is EventActivity) {
            (requireActivity() as EventActivity).changeActionBarTitle(event.title)
        }

        // Title
        txtTitle?.text = event.title
        // Subtitle
        if(StringUtils.isNotEmpty(event.subtitle)){
            txtSubtitle?.text = event.subtitle
            txtSubtitle?.visibility = View.VISIBLE
        } else {
            txtSubtitle?.visibility = View.GONE
        }

        // Description
        if(StringUtils.isNotEmpty(event.description)){
            txtDescription?.text = event.description
            txtDescription?.visibility = View.VISIBLE
        } else {
            txtDescription?.visibility = View.GONE
        }

        // Identifier
        txtIdentifier?.text = getString(R.string.events_identifier, event.id)

        // Image
        event.image?.let {
            Picasso.get().load(it.link()).into(imgMain)
            imgMain?.setOnClickListener { MyUtils.zoomToThisImage(requireContext(), event.image!!) }
        }

        // Init date
        if(event.initDate != null){
            txtInitDate?.text = getString(R.string.events_init, sdf.format(event.initDate))
            txtInitDate?.visibility = View.VISIBLE
        } else {
            txtInitDate?.visibility = View.GONE
        }

        // End date
        if(event.endDate != null){
            txtEndDate?.text = getString(R.string.events_end, sdf.format(event.endDate), MyUtils.getTimeToFinish(requireContext(), event.endDate))
            txtEndDate?.visibility = View.VISIBLE
        } else {
            txtEndDate?.visibility = View.GONE
        }

        // Event type
        if(event.type != null) {
            txtEventType?.text = getString(R.string.event_type, getEventTypeName(event.type!!))
            txtEventType?.visibility = View.VISIBLE
        } else {
            txtEventType?.visibility = View.GONE
        }

        // Num participants
        if(event.numParticipants != null) {
            txtNumParticipants?.text = getString(R.string.events_num_participants, event.numParticipants)
            txtNumParticipants?.visibility = View.VISIBLE
        } else {
            txtNumParticipants?.visibility = View.GONE
        }

        // Max places
        if(event.numParticipants != null && event.maxPlaces != null) {
            txtAvailablePlaces?.text = getString(R.string.events_available_places, event.maxPlaces!! - event.numParticipants!!)
            txtAvailablePlaces?.visibility = View.VISIBLE
        } else {
            txtAvailablePlaces?.visibility = View.GONE
        }

        // Rewards
        if(event.rewards != null && event.rewards!!.isNotEmpty()) {
            val rewards = getSortedRewards(event.rewards!!)
            rewardsRecyclerView?.adapter = ListRewardAdapter(requireContext(), rewards)
            rewardsRecyclerView?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.label_event_detail_rewards)?.visibility = View.VISIBLE
        } else {
            rewardsRecyclerView?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.label_event_detail_rewards)?.visibility = View.GONE
        }

        // Punishments
        if(event.punishments != null && event.punishments!!.isNotEmpty()) {
            val punishments = getSortedPunishments(event.punishments!!)
            punishmentsRecyclerView?.adapter = ListPunishmentAdapter(requireContext(), punishments)
            punishmentsRecyclerView?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.label_event_detail_punishments)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.divider_event_detail_punishments)?.visibility = View.VISIBLE
        } else {
            punishmentsRecyclerView?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.label_event_detail_punishments)?.visibility = View.GONE
            view?.findViewById<View>(R.id.divider_event_detail_punishments)?.visibility = View.GONE
        }

        // Button main
        btnJoin?.setOnClickListener { joinToEvent(event, false) }
        setButtonText(event, null)

        // Button reject
        if(showRejectButton == true) {
            btnReject?.setOnClickListener { joinToEvent(event, true) }
            btnReject?.visibility = View.VISIBLE
        }

        progressBar?.visibility = View.GONE
        scrollView?.visibility = View.VISIBLE

    }

    private fun getSortedRewards(rewards: List<RewardDTO>): List<RewardDTO> {
        return rewards.sortedBy { r -> r.requiredPosition }
    }

    private fun getSortedPunishments(punishments: List<PunishmentDTO>): List<PunishmentDTO> {
        return punishments.sortedBy { r -> r.requiredPosition }
    }

    private fun getEventTypeName(type: EventType): String {
        return when(type) {
            EventType.SPORTS -> getString(R.string.events_sports)
            EventType.VIDEOGAMES -> getString(R.string.events_sports)
            EventType.FAMILY -> getString(R.string.events_family)
            EventType.ACADEMIC -> getString(R.string.events_academic)
            else -> getString(R.string.events_other)
        }
    }

    private fun joinToEvent(event: EventDTO, reject: Boolean = false) {
        val request  = EventUserPostDTO(UserAccount.getInstance(requireContext()).getName())
        request.reject = reject
        eventService.addUser(event.id, request, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventUserDTO> {
            override fun onResponse(call: Call<EventUserDTO>, response: Response<EventUserDTO>) {
                if(response.code() == HttpURLConnection.HTTP_CREATED){
                    if(!reject) {
                        if(event.approvalNeeded == true){
                            showSuccessDialog(getString(R.string.events_user_request_join))
                        } else {
                            showSuccessDialog(getString(R.string.events_user_join_success))
                        }
                    } else {
                        showSuccessDialog(getString(R.string.events_user_request_reject))
                    }

                    btnReject?.visibility = View.GONE
                    setButtonText(event, UserAccount.getInstance(requireContext()).getName())
                } else {
                    try {
                        val errorDto = Gson().fromJson(response.errorBody()?.string(), ErrorDTO::class.java) as ErrorDTO
                        showErrorDialog(getString(Message.forCode(errorDto.message)))
                    } catch (e : Exception) {
                        showErrorDialog(getString(R.string.error_api_undefined))
                    }
                }
            }
            override fun onFailure(call: Call<EventUserDTO>, t: Throwable) {
                showErrorDialog(getString(R.string.error_api_undefined))
            }
        })
    }

    private fun setButtonText(event: EventDTO, userId : String?) {
        val user = searchUser(event.users, UserAccount.getInstance(requireContext()).getName())
        if(user == null && userId == null) {
            btnJoin?.text = getString(R.string.events_join)
            if(event.inscription == EventInscriptionType.PRIVATE){
                btnJoin?.visibility = View.GONE
                txtEventJoined?.visibility = View.VISIBLE
                txtEventJoined?.text = getString(R.string.events_private)

            } else if(event.approvalNeeded == true) {
                btnJoin?.text = getString(R.string.events_request_join)
            }
        } else {
            val requestId = user?.id ?: userId
            eventService.findEventAndUser(event.id, requestId!!, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<EventUserDTO> {
                override fun onResponse(call: Call<EventUserDTO>, response: Response<EventUserDTO>) {
                    if(response.code() == HttpURLConnection.HTTP_OK){
                        val eventUser : EventUserDTO? = response.body()
                        when (eventUser?.status) {
                            EventUserStatusType.ACCEPTED -> {
                                btnJoin?.visibility = View.GONE
                                txtEventJoined?.visibility = View.VISIBLE
                                txtEventJoined?.text = getString(R.string.events_joined)
                            }
                            EventUserStatusType.INVITED -> {
                                btnJoin?.text = getString(R.string.events_accept_invitation)
                            }
                            EventUserStatusType.WAITING_APPROVAL -> {
                                btnJoin?.visibility = View.GONE
                                txtEventJoined?.visibility = View.VISIBLE
                                txtEventJoined?.text = getString(R.string.events_waiting_approval)
                            }
                            EventUserStatusType.REJECTED -> {
                                btnJoin?.visibility = View.GONE
                                txtEventJoined?.visibility = View.VISIBLE
                                txtEventJoined?.text = getString(R.string.events_request_rejected)
                            }
                            EventUserStatusType.DELETED -> {
                                btnJoin?.visibility = View.GONE
                                txtEventJoined?.visibility = View.VISIBLE
                                txtEventJoined?.text = getString(R.string.events_user_deleted)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<EventUserDTO>, t: Throwable) {
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is EventActivity){
            val eventActivity = requireActivity() as EventActivity
            if(eventActivity.wasPaused){
                loadEvent(eventId!!)
                Log.e("Resume", "recarga evento")
            }
        }
    }

    fun searchUser(users : List<UserLiteWithEventDTO>?, username : String) : UserLiteWithEventDTO? {
        users?.forEach {
            if(it.id == username)
                return it
        }
        return null;
    }

    private fun showSuccessDialog(message : String) {
        MyDialog.message(this, getString(R.string.events), message)
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}