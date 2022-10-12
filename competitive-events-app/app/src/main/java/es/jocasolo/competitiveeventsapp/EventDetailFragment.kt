package es.jocasolo.competitiveeventsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.ListRewardAdapter
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
class EventDetailFragment : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)

    private var rewardsRecyclerView: RecyclerView? = null

    private var txtTitle : TextView? = null
    private var txtSubtitle : TextView? = null
    private var txtDescription : ReadMoreTextView? = null
    private var txtInitDate : TextView? = null
    private var txtEndDate : TextView? = null
    private var txtNumParticipants : TextView? = null
    private var txtAvailablePlaces : TextView? = null
    private var imgMain : ImageView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        rewardsRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_reward_list)
        rewardsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        // Init input fields
        txtTitle = view.findViewById(R.id.txt_event_detail_title)
        txtSubtitle = view.findViewById(R.id.txt_event_detail_subtitle)
        txtDescription = view.findViewById(R.id.txt_event_detail_description)
        txtInitDate = view.findViewById(R.id.txt_event_detail_init_date)
        txtEndDate = view.findViewById(R.id.txt_event_detail_end_date)
        txtNumParticipants = view.findViewById(R.id.txt_event_detail_num_participants)
        txtAvailablePlaces = view.findViewById(R.id.txt_event_detail_available_places)
        imgMain = view.findViewById(R.id.img_event_detail_main)

        // Show event detail
        val id = arguments?.getString("eventId")
        if(id != null) {
            loadEvent(id)
        }

        super.onViewCreated(view, savedInstanceState)
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
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = event.title

        // Title
        txtTitle?.text = event.title
        // Subtitle
        if(StringUtils.isNotEmpty(event.subtitle)){
            txtSubtitle?.text = event.subtitle
        } else {
            txtSubtitle?.visibility = View.GONE
        }
        // Description
        if(StringUtils.isNotEmpty(event.description)){
            txtDescription?.text = event.description
        } else {
            txtDescription?.visibility = View.GONE
        }
        // Image
        event.image?.let {
            Picasso.get().load(it.link()).into(imgMain)
        }
        // Init date
        if(event.initDate != null){
            txtInitDate?.text = getString(R.string.events_init, sdf.format(event.initDate))
        } else {
            txtInitDate?.visibility = View.GONE
        }
        // End date
        if(event.endDate != null){
            txtEndDate?.text = getString(R.string.events_end, sdf.format(event.endDate), MyUtils.getTimeToFinish(requireContext(), event.endDate))
        } else {
            txtEndDate?.visibility = View.GONE
        }
        // Num participants
        if(event.numParticipants != null) {
            txtNumParticipants?.text = getString(R.string.events_num_participants, event.numParticipants)
        } else {
            txtNumParticipants?.visibility = View.GONE
        }
        // Max places
        if(event.numParticipants != null && event.maxPlaces != null) {
            txtAvailablePlaces?.text = getString(R.string.events_available_places, event.maxPlaces!! - event.numParticipants!!)
        } else {
            txtAvailablePlaces?.visibility = View.GONE
        }
        // Rewards
        if(event.rewards != null) {
            val rewards = getSortedRewards(event.rewards!!)
            rewardsRecyclerView?.adapter = ListRewardAdapter(requireContext(), rewards)
        } else {
            rewardsRecyclerView?.visibility = View.GONE
        }
    }

    private fun getSortedRewards(rewards: List<RewardDTO>): List<RewardDTO> {
        return rewards.sortedBy { r -> r.requiredPosition }
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}