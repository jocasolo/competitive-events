package es.jocasolo.competitiveeventsapp.fragment.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.dto.ErrorDTO
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.comment.CommentDTO
import es.jocasolo.competitiveeventsapp.dto.comment.CommentPostDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScoreDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScorePostDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScorePutDTO
import es.jocasolo.competitiveeventsapp.enums.event.EventStatusType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import es.jocasolo.competitiveeventsapp.fragment.BackStackListener
import es.jocasolo.competitiveeventsapp.fragment.score.ScoreCreationDialogFragment
import es.jocasolo.competitiveeventsapp.service.CommentService
import es.jocasolo.competitiveeventsapp.service.EventService
import es.jocasolo.competitiveeventsapp.service.ScoreService
import es.jocasolo.competitiveeventsapp.service.ServiceBuilder
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.ui.adapters.ListHistoricAdapter
import es.jocasolo.competitiveeventsapp.utils.Message
import es.jocasolo.competitiveeventsapp.utils.MyDialog
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import okhttp3.MultipartBody
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EventMainFragment(var eventId: String? = null) : Fragment(), BackStackListener {

    private val eventService = ServiceBuilder.buildService(EventService::class.java)
    private val commentService = ServiceBuilder.buildService(CommentService::class.java)
    private val scoreService = ServiceBuilder.buildService(ScoreService::class.java)

    private var recyclerView: RecyclerView? = null
    private var historicalAdapter : ListHistoricAdapter? = null

    private var txtCommentCreate: EditText? = null
    private var imgCommentCreate: ImageView? = null
    private var imgScoreCreate: ImageView? = null

    var id : String? = null

    /**
     * Action called when the score dialog is closed
     */
    override fun backStackAction(data: BackStackEntryDTO) {
        if(data is ScorePostDTO) {
            val score = data as ScorePostDTO
            createScore(score)
        }
        if(data is ScorePutDTO) {
            loadEvent(eventId!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Show event detail
        id = arguments?.getString("eventId")
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

        // Event list
        recyclerView = requireView().findViewById(R.id.recycler_event_main)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        if(id != null) {
            loadEvent(id!!)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun openCreateScoreFragment(scoreValueType: ScoreValueType) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        val prev = parentFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newDialogFragment: ScoreCreationDialogFragment = ScoreCreationDialogFragment(this, scoreValueType)
        newDialogFragment.show(ft, "dialog")
    }

    private fun loadEvent(id: String) {
        eventService.findOne(id, UserAccount.getInstance(requireContext()).getToken()).enqueue(
            object :
                Callback<EventDTO> {
                override fun onResponse(call: Call<EventDTO>, response: Response<EventDTO>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val event = response.body()
                        if (event != null) {
                            loadHistorical(event)
                            imgCommentCreate?.setOnClickListener { createComment(event.id) }
                            if(event.status == EventStatusType.ACTIVE) {
                                imgScoreCreate?.setOnClickListener { openCreateScoreFragment(event.scoreType!!) }
                            } else {
                                imgScoreCreate?.visibility = View.GONE
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

                override fun onFailure(call: Call<EventDTO>, t: Throwable) {
                    showErrorDialog(getString(R.string.error_api_undefined))
                }
            })
    }

    private fun loadHistorical(event: EventDTO) {
        val historical : MutableList<HistoryItemDTO> = mutableListOf()
        val username = UserAccount.getInstance(requireContext()).getName()

        // Init event
        event.initDate = if(event.initDate != null) event.initDate else event.creationDate
        if(event.initDate != null && event.initDate!!.before(Date())) {
            val history = HistoryItemDTO()
            history.historyType = HistoryItemDTO.HistoryItemType.INIT_EVENT
            history.sortDate = event.initDate
            historical.add(history)
        }
        // Comments
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
        // Scores
        event.scores?.forEach {
            it.historyType = HistoryItemDTO.HistoryItemType.SCORE
            it.sortDate = it.date
            historical.add(it)
        }
        // Users
        event.users?.forEach {
            if(it.status == EventUserStatusType.ACCEPTED) {
                it.historyType = HistoryItemDTO.HistoryItemType.USER_JOIN
                it.sortDate = it.incorporationDate
                historical.add(it)
            }
        }
        var addMillis = 0;
        // Finish event
        if(event.endDate != null && event.status == EventStatusType.FINISHED) {
            val history = HistoryItemDTO()
            history.historyType = HistoryItemDTO.HistoryItemType.END_EVENT
            event.endDate?.time = event.endDate?.time!! + addMillis++;
            history.sortDate = event.endDate
            historical.add(history)
        }
        // Rewards
        event.rewards?.forEach {
            if(StringUtils.isNotEmpty(it.winner?.id)){
                it.historyType = HistoryItemDTO.HistoryItemType.WINNER
                event.endDate?.time = event.endDate?.time!! + addMillis++;
                it.sortDate = event.endDate
                historical.add(it)
            }
        }
        // Punishments
        event.punishments?.forEach {
            if(StringUtils.isNotEmpty(it.looser?.id)){
                it.historyType = HistoryItemDTO.HistoryItemType.LOOSER
                event.endDate?.time = event.endDate?.time!! + addMillis++;
                it.sortDate = event.endDate
                historical.add(it)
            }
        }

        // Sort by date
        historical.sortBy { it.sortDate }

        historicalAdapter = ListHistoricAdapter(this, historical, event)
        recyclerView?.adapter = historicalAdapter
        historicalAdapter?.notifyDataSetChanged()

        // Scroll to bottom
        recyclerView?.scrollToPosition(historical.size - 1);
    }

    private fun createComment(id: String) {
        MyUtils.closeKeyboard(this.requireContext(), requireView())
        val comment = CommentPostDTO(txtCommentCreate?.text.toString(), id)

        commentService.create(comment, UserAccount.getInstance(requireContext()).getToken()).enqueue(
            object : Callback<CommentDTO> {
                override fun onResponse(call: Call<CommentDTO>, response: Response<CommentDTO>) {
                    val newComment = response.body()
                    if (newComment != null) {
                        loadEvent(eventId!!)
                        txtCommentCreate?.text = null
                    }
                }

                override fun onFailure(call: Call<CommentDTO>, t: Throwable) {
                }
            })
    }

    private fun createScore(score: ScorePostDTO) {
        score.eventId = id!!
        scoreService.create(score, UserAccount.getInstance(requireContext()).getToken()).enqueue(
            object : Callback<ScoreDTO> {
                override fun onResponse(call: Call<ScoreDTO>, response: Response<ScoreDTO>) {
                    val newScore = response.body()
                    if (newScore != null) {
                        if(score.imagePart != null) {
                            uploadScoreImage(newScore.id, score.imagePart)
                        } else {
                            loadEvent(eventId!!)
                        }
                    }
                }

                override fun onFailure(call: Call<ScoreDTO>, t: Throwable) {
                }
            })
    }

    private fun uploadScoreImage(id : String, imagePart : MultipartBody.Part?) {
        imagePart?.let {
            scoreService.updateImage(it, id.toInt(), UserAccount.getInstance(requireContext()).getToken()).enqueue(object : Callback<ScoreDTO> {
                override fun onResponse(call: Call<ScoreDTO>, response: Response<ScoreDTO>) {
                    val newScore = response.body()
                    if (newScore != null) {
                        loadEvent(eventId!!)
                    }
                }
                override fun onFailure(call: Call<ScoreDTO>, t: Throwable) {
                }
            })
        }
    }

    private fun showErrorDialog(message: String) {
        MyDialog.message(this, getString(R.string.error_title), message)
    }

}