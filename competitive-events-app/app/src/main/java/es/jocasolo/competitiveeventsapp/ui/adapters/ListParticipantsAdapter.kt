package es.jocasolo.competitiveeventsapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ParticipantDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import java.lang.String
import java.time.Duration


open class ListParticipantsAdapter(
    var context: Context,
    var participants: List<ParticipantDTO>,
    var scoreValueType: ScoreValueType
): RecyclerView.Adapter<ListParticipantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.card_layout_participant,
            parent,
            false
        )

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(participants.size > position) {
            val participant = participants[position]
            participant.let {
                holder.itemName.text = participant.name+", " + getPrivilege(participant.privilege)
                holder.itemScore.text = getScoreValue(participant.score)
                if(participant.image != null) {
                    holder.itemImage.imageTintMode = null
                    Picasso.get()
                        .load(participant.image)
                        .resize(100, 100)
                        .centerCrop()
                        .error(R.drawable.military_tech)
                        .into(holder.itemImage)
                }
            }
        }
    }

    private fun getScoreValue(score: Double?): CharSequence? {
        return when(scoreValueType) {
            ScoreValueType.NUMERIC -> score?.toInt().toString()
            ScoreValueType.DECIMAL -> score?.toDouble().toString()
            ScoreValueType.TIME -> getTime(score)
            else -> score.toString()
        }
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
                timeInHHMMSS = String.format("%02d:%02d:%02d %s", hh, mm, ss, context.getString(R.string.hours))
            } else if (mm > 0) {
                timeInHHMMSS = String.format("%02d:%02d  %s", mm, ss, context.getString(R.string.minutes))
            } else {
                timeInHHMMSS = String.format("%02d  %s", ss, context.getString(R.string.seconds))
            }
        }
        return timeInHHMMSS
    }

    private fun getPrivilege(privilege: EventUserPrivilegeType?): Any? {
        return when(privilege) {
            EventUserPrivilegeType.USER -> context.getString(R.string.user)
            EventUserPrivilegeType.OWNER -> context.getString(R.string.owner)
            else -> context.getString(R.string.user)
        }
    }

    override fun getItemCount(): Int {
        participants.let {
            return participants.size
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView = itemView.findViewById(R.id.img_participant_avatar)
        var itemEdit: ImageView = itemView.findViewById(R.id.img_participant_edit)
        var itemName: TextView = itemView.findViewById(R.id.txt_participant_name)
        var itemScore: TextView = itemView.findViewById(R.id.txt_participant_score)

    }

}