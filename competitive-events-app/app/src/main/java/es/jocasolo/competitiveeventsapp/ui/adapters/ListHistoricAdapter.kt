package es.jocasolo.competitiveeventsapp.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.comment.CommentDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.image.ImageDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScoreDTO
import es.jocasolo.competitiveeventsapp.dto.user.UserLiteWithEventDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreStatusType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import es.jocasolo.competitiveeventsapp.fragment.event.EventMainFragment
import es.jocasolo.competitiveeventsapp.fragment.score.ScoreEditDialogFragment
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import es.jocasolo.competitiveeventsapp.utils.MyUtils
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*


open class ListHistoricAdapter(
    var fragment: EventMainFragment,
    var historical: MutableList<HistoryItemDTO>?,
    var eventDTO: EventDTO
): RecyclerView.Adapter<ListHistoricAdapter.ViewHolder>() {

    private val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.card_layout_history,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(historical != null && historical!!.size > position) {
            val history = historical?.get(position)
            history?.let {


                when (history.historyType){
                    // Comment another user
                    HistoryItemDTO.HistoryItemType.COMMENT_USER -> {
                        val comment = history as CommentDTO
                        holder.layoutUserComment.visibility = View.VISIBLE
                        holder.txtUserCommentText.text = comment.text
                        holder.txtUserCommentDate.text = getDateText(comment.user?.id, comment.date)
                        loadAvatarImage(comment.user?.avatar?.link(), holder.imgUserCommentAvatar)
                    }
                    // Comment own
                    HistoryItemDTO.HistoryItemType.COMMENT_OWN -> {
                        val comment = history as CommentDTO
                        holder.layoutOwnComment.visibility = View.VISIBLE
                        holder.txtOwnCommentText.text = comment.text
                        holder.txtOwnCommentDate.text = getDateText(comment.user?.id, comment.date)
                        loadAvatarImage(comment.user?.avatar?.link(), holder.imgOwnCommentAvatar)
                    }
                    // Score
                    HistoryItemDTO.HistoryItemType.SCORE -> {
                        val score = history as ScoreDTO
                        holder.layoutScore.visibility = View.VISIBLE
                        if (eventDTO.scoreType != ScoreValueType.TIME) {
                            holder.txtScoreText.text = fragment.getString(
                                R.string.user_new_score,
                                score.user?.id,
                                score.value
                            )
                        } else {
                            holder.txtScoreText.text = fragment.getString(
                                R.string.user_new_score, score.user?.id, getTime(
                                    score.value?.toDouble()
                                )
                            )
                        }
                        holder.txtScoreDate.text = getDateText(score.user?.id, score.date)
                        loadAvatarImage(score.user?.avatar?.link(), holder.imgScoreAvatar)
                        if (score.image != null) {
                            holder.imgScoreImage.visibility = View.VISIBLE
                            loadAvatarImage(score.image?.link(), holder.imgScoreImage, 150, 100)
                            holder.imgScoreImage.setOnClickListener {
                                MyUtils.zoomToThisImage(fragment.requireActivity(), score.image!! )
                            }
                        }
                        if (MyUtils.isAdmin(
                                eventDTO.users,
                                UserAccount.getInstance(fragment.requireContext()).getName()
                            )
                        ) {
                            holder.btnScoreEdit.setOnClickListener { openScoreEditDialog(score) }
                            holder.btnScoreEdit.visibility = View.VISIBLE
                        }
                        if (score.status == ScoreStatusType.NOT_VALID) {
                            holder.txtScoreNotValid.visibility = View.VISIBLE
                        }
                    }
                    // User join
                    HistoryItemDTO.HistoryItemType.USER_JOIN -> {
                        val user = history as UserLiteWithEventDTO
                        holder.layoutNotification.visibility = View.VISIBLE
                        holder.txtNotificationDate.text = getDateText(null, user.incorporationDate)
                        holder.txtNotificationTitle.text = fragment.getString(
                            R.string.user_join_notification,
                            user.id
                        )
                        holder.txtNotificationText.visibility = View.GONE
                        holder.imgNotificationImage.setImageResource(R.drawable.person_add)
                    }
                    // Winner
                    /*HistoryItemDTO.HistoryItemType.WINNER -> {
                        val reward = history as RewardDTO
                        holder.layoutNotification.visibility = View.VISIBLE
                        holder.txtWinnerDate.text = getDateText(null, history.sortDate)
                        holder.txtWinnerTitle.text = fragment.getString(R.string.winner_notification, reward.winner?.id)
                        holder.txtWinnerText2.text = reward.title
                    }
                    // Looser
                    HistoryItemDTO.HistoryItemType.LOOSER -> {
                        val punishment = history as PunishmentDTO
                        holder.imgNotificationImage.setImageResource(R.drawable.mood_bad)
                        holder.layoutWinner.visibility = View.VISIBLE
                        holder.txtWinnerDate.text = getDateText(null, history.sortDate)
                        holder.txtWinnerTitle.text = fragment.getString(R.string.looser_notification, punishment.looser?.id)
                        holder.txtWinnerText2.text = punishment.title
                    }*/
                    HistoryItemDTO.HistoryItemType.INIT_EVENT -> {
                        val h = history as HistoryItemDTO
                        holder.layoutNotification.visibility = View.VISIBLE
                        holder.txtNotificationDate.text = getDateText(null, h.sortDate)
                        holder.txtNotificationTitle.text = fragment.getString(R.string.init_event)
                        holder.txtNotificationText.visibility = View.GONE
                        holder.imgNotificationImage.setImageResource(R.drawable.play_arrow)
                    }
                    HistoryItemDTO.HistoryItemType.END_EVENT -> {
                        val h = history as HistoryItemDTO
                        holder.layoutNotification.visibility = View.VISIBLE
                        holder.txtNotificationDate.text = getDateText(null, h.sortDate)
                        holder.txtNotificationTitle.text = fragment.getString(R.string.end_event)
                        holder.txtNotificationText.visibility = View.GONE
                        holder.imgNotificationImage.setImageResource(R.drawable.block)
                    }
                }
            }
        }
    }

    private fun zoomToThisImage(image: ImageDTO) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(image.link()), "image/*")
        fragment.requireActivity().startActivity(intent)
    }

    private fun openScoreEditDialog(score: ScoreDTO) {
        val ft: FragmentTransaction = fragment.parentFragmentManager.beginTransaction()
        val prev = fragment.parentFragmentManager.findFragmentByTag("dialogScoreEdit")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newDialogFragment = ScoreEditDialogFragment(fragment, score, fragment.eventId!!)
        newDialogFragment.show(ft, "dialogScoreEdit")
    }

    private fun getDateText(id: String?, date: Date?): CharSequence? {
        var result = ""
        if(id != null) {
            result += "$id, "
        }
        if(date != null) {
            result += sdf.format(date)
        }
       return result
    }

    private fun loadAvatarImage(
        imageLink: String?,
        imageView: ImageView?,
        width: Int = 30,
        height: Int = 30
    ) {
        if (StringUtils.isNotEmpty(imageLink)) {
            Picasso.get()
                .load(imageLink)
                .resize(width, height)
                .centerCrop()
                .error(R.drawable.person)
                .into(imageView)
            imageView?.imageTintMode = null
        }
    }

    override fun getItemCount(): Int {
        if(!historical.isNullOrEmpty()){
            return historical!!.size
        }
        return 0;
    }

    fun addHistory(newHistory: HistoryItemDTO){
        if(historical == null) {
            historical = mutableListOf()
        }
        historical?.add(newHistory)
    }

    fun addHistorical(newHistorical: List<HistoryItemDTO>){
        if(historical == null) {
            historical = mutableListOf()
        }
        historical?.addAll(newHistorical)
    }

    private fun getTime(score: Double?): CharSequence? {
        var timeInHHMMSS = score.toString()
        if(score != null) {
            val duration: Duration = Duration.ofMillis(score.toLong())
            val seconds = duration.seconds
            val hh = seconds / 3600
            val mm = seconds % 3600 / 60
            val ss = seconds % 60
            if(hh > 0) {
                timeInHHMMSS = String.format(
                    "%02d:%02d:%02d %s", hh, mm, ss, fragment.requireContext().getString(
                        R.string.hours
                    )
                )
            } else if (mm > 0) {
                timeInHHMMSS = String.format(
                    "%02d:%02d  %s", mm, ss, fragment.requireContext().getString(
                        R.string.minutes
                    )
                )
            } else {
                timeInHHMMSS = String.format(
                    "%02d  %s",
                    ss,
                    fragment.requireContext().getString(R.string.seconds)
                )
            }
        }
        return timeInHHMMSS
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // Card comment user
        var layoutUserComment: ConstraintLayout = itemView.findViewById(R.id.layout_user_comment)
        var txtUserCommentText: TextView = itemView.findViewById(R.id.txt_user_comment_text)
        var txtUserCommentDate: TextView = itemView.findViewById(R.id.txt_user_comment_date)
        var imgUserCommentAvatar: ImageView = itemView.findViewById(R.id.img_user_comment_avatar)

        // Card comment own
        var layoutOwnComment: ConstraintLayout = itemView.findViewById(R.id.layout_own_comment)
        var txtOwnCommentText: TextView = itemView.findViewById(R.id.txt_own_comment_text)
        var txtOwnCommentDate: TextView = itemView.findViewById(R.id.txt_own_comment_date)
        var imgOwnCommentAvatar: ImageView = itemView.findViewById(R.id.img_own_comment_avatar)

        // Card score
        var layoutScore: ConstraintLayout = itemView.findViewById(R.id.layout_score)
        var txtScoreText: TextView = itemView.findViewById(R.id.txt_score_text)
        var txtScoreDate: TextView = itemView.findViewById(R.id.txt_score_date)
        var imgScoreAvatar: ImageView = itemView.findViewById(R.id.img_score_avatar)
        var imgScoreImage: ImageView = itemView.findViewById(R.id.img_score_image)
        var btnScoreEdit: ImageView = itemView.findViewById(R.id.btn_score_edit)
        var txtScoreNotValid: TextView = itemView.findViewById(R.id.txt_score_not_valid)

        // Card notification
        var layoutNotification : ConstraintLayout = itemView.findViewById(R.id.layout_notification)
        var txtNotificationTitle: TextView = itemView.findViewById(R.id.txt_notification_title)
        var txtNotificationText: TextView = itemView.findViewById(R.id.txt_notification_text)
        var txtNotificationDate: TextView = itemView.findViewById(R.id.txt_notification_date)
        var imgNotificationImage: ImageView = itemView.findViewById(R.id.img_notification_image)

        // Card winner
        var layoutWinner : ConstraintLayout = itemView.findViewById(R.id.layout_winner)
        var txtWinnerTitle: TextView = itemView.findViewById(R.id.txt_winner_title)
        var txtWinnerText: TextView = itemView.findViewById(R.id.txt_winner_text)
        var txtWinnerText2: TextView = itemView.findViewById(R.id.txt_winner_text)
        var txtWinnerDate: TextView = itemView.findViewById(R.id.txt_winner_date)
        var imgWinnerImage: ImageView = itemView.findViewById(R.id.img_winner_image)
        var imgWinnerRewardImage: ImageView = itemView.findViewById(R.id.img_winner_reward_image)
    }

}