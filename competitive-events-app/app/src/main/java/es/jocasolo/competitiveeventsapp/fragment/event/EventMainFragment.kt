package es.jocasolo.competitiveeventsapp.fragment.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.comment.CommentDTO
import es.jocasolo.competitiveeventsapp.dto.comment.CommentPostDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.service.CommentService
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ListHistoricAdapter
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventMainFragment(var eventId: String? = null) : Fragment() {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)
    private val commentService = ServiceBuilder.buildService(CommentService::class.java)

    private var recyclerView: RecyclerView? = null
    private var historicalAdapter : ListHistoricAdapter? = null

    private var txtCommentCreate: EditText? = null
    private var imgCommentCreate: ImageView? = null
    private var imgScoreCreate: ImageView? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Show event detail
        var id = arguments?.getString("eventId")
        if(id == null) id = eventId

        // Comment edit text
        txtCommentCreate = view.findViewById(R.id.txt_comment_create)
        imgCommentCreate = view.findViewById(R.id.img_comment_create)
        imgScoreCreate = view.findViewById(R.id.img_score_create)
        txtCommentCreate?.addTextChangedListener {
            if(StringUtils.isNotEmpty(txtCommentCreate?.text)) {
                imgCommentCreate?.visibility = View.VISIBLE
                imgScoreCreate?.visibility = View.GONE
            } else {
                imgCommentCreate?.visibility = View.GONE
                imgScoreCreate?.visibility = View.VISIBLE
            }
        }

        imgCommentCreate?.setOnClickListener { createComment(id!!) }

        // Event list
        if(historicalAdapter == null){
            historicalAdapter = ListHistoricAdapter(this, null)
        }
        recyclerView = requireView().findViewById(R.id.recycler_event_main)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = historicalAdapter

        if(id != null) {
            loadEvent(id)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadEvent(id: String) {
        eventService.findOne(id, UserAccount.getInstance(requireContext()).getToken()).enqueue(object :
            Callback<EventDTO> {
            override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val event = response.body()
                    if (event != null) {
                        loadHistorical(event)
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

    private fun loadHistorical(event: EventDTO) {
        val historical : MutableList<HistoryItemDTO> = mutableListOf()
        val username = UserAccount.getInstance(requireContext()).getName()
        event.comments?.forEach {
            if (username == it.user?.id) {
                it.historyType = HistoryItemDTO.HistoryItemType.COMMENT_OWN
                it.sortDate = it.date
            } else {
                it.historyType = HistoryItemDTO.HistoryItemType.COMMENT_USER
                it.sortDate = it.date
            }
            historical.add(it)
        }
        event.scores?.forEach {
            it.historyType = HistoryItemDTO.HistoryItemType.SCORE
            it.sortDate = it.date
            historical.add(it)
        }

        historical.sortBy { it.sortDate }

        historicalAdapter?.addHistorical(historical)
        historicalAdapter?.notifyDataSetChanged()

        // Scroll to bottom
        historicalAdapter?.itemCount?.minus(1)?.let { recyclerView?.scrollToPosition(it) }

    }

    private fun createComment(id: String) {
        MyUtils.closeKeyboard(this.requireContext(), requireView())
        val comment = CommentPostDTO(txtCommentCreate?.text.toString(), id)

        commentService.create(comment, UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<CommentDTO> {
            override fun onResponse(call: Call<CommentDTO>, response: Response<CommentDTO>) {
                val newComment = response.body()
                if(newComment != null) {
                    newComment.sortDate = newComment.date
                    newComment.historyType = HistoryItemDTO.HistoryItemType.COMMENT_OWN
                    historicalAdapter?.addHistory(newComment)
                    historicalAdapter?.notifyDataSetChanged()
                    txtCommentCreate?.text = null

                    // Scroll to bottom
                    historicalAdapter?.itemCount?.minus(1)?.let { recyclerView?.scrollToPosition(it) }
                }
            }
            override fun onFailure(call: Call<CommentDTO>, t: Throwable) {
            }
        })
    }


    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }
}