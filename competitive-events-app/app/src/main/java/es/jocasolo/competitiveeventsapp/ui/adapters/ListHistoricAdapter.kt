package es.jocasolo.competitiveeventsapp.ui.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.HistoryItemDTO
import es.jocasolo.competitiveeventsapp.dto.comment.CommentDTO
import es.jocasolo.competitiveeventsapp.dto.score.ScoreDTO
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat
import java.util.*

open class ListHistoricAdapter(
    var fragment: Fragment,
    var historical: MutableList<HistoryItemDTO>?
): RecyclerView.Adapter<ListHistoricAdapter.ViewHolder>() {

    private val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.card_layout_historic,
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
                    HistoryItemDTO.HistoryItemType.COMMENT_USER -> {
                        val comment = history as CommentDTO
                        holder.layoutUserComment.visibility = View.VISIBLE
                        holder.txtUserCommentText.text = comment.text
                        holder.txtUserCommentDate.text = getDateText(comment.user?.id, comment.date)
                        loadAvatarImage(comment.user?.avatar?.link(), holder.imgUserCommentAvatar)
                    }
                    HistoryItemDTO.HistoryItemType.COMMENT_OWN -> {
                        val comment = history as CommentDTO
                        holder.layoutOwnComment.visibility = View.VISIBLE
                        holder.txtOwnCommentText.text = comment.text
                        holder.txtOwnCommentDate.text = getDateText(comment.user?.id, comment.date)
                        loadAvatarImage(comment.user?.avatar?.link(), holder.imgOwnCommentAvatar)
                    }
                    HistoryItemDTO.HistoryItemType.SCORE -> {
                        val score = history as ScoreDTO
                        holder.layoutScore.visibility = View.VISIBLE
                        holder.txtScoreText.text = fragment.getString(R.string.user_new_score, score.user?.id, score.value)
                        holder.txtScoreDate.text = getDateText(score.user?.id, score.date)
                        loadAvatarImage(score.user?.avatar?.link(), holder.imgScoreAvatar)
                        if(score.image != null) {
                            holder.imgScoreImage.visibility = View.VISIBLE
                            loadAvatarImage(score.image?.link(), holder.imgScoreImage, 150, 100)
                        }
                    }
                }
            }
        }
    }

    private fun getDateText(id: String?, date: Date?): CharSequence? {
        var result = id
        if(date != null) {
            result += ", " + sdf.format(date)
        }
       return result
    }

    private fun loadAvatarImage(imageLink: String?, imageView: ImageView?, width: Int = 30, height : Int = 30) {
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

    }

}