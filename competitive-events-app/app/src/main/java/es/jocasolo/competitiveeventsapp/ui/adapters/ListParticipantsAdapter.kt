package es.jocasolo.competitiveeventsapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ParticipantDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType

open class ListParticipantsAdapter(var context : Context, var participants: List<ParticipantDTO>): RecyclerView.Adapter<ListParticipantsAdapter.ViewHolder>() {

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
                holder.itemName.text = participant.name
                holder.itemScore.text = participant.score.toString()
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

    override fun getItemCount(): Int {
        participants.let {
            return participants.size
        }
    }

    private fun getSortRequiredPosition(reward: RewardDTO): Int {
        reward.sortScore?.let {
            if(it == ScoreSortType.ASC)
                return R.string.higher
        }
        return R.string.lower
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView = itemView.findViewById(R.id.img_participant_avatar)
        var itemEdit: ImageView = itemView.findViewById(R.id.img_participant_edit)
        var itemName: TextView = itemView.findViewById(R.id.txt_participant_name)
        var itemScore: TextView = itemView.findViewById(R.id.txt_participant_score)

    }

}