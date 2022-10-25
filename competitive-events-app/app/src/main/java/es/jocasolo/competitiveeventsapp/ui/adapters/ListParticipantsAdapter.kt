package es.jocasolo.competitiveeventsapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.ParticipantDTO
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserPrivilegeType
import es.jocasolo.competitiveeventsapp.enums.eventuser.EventUserStatusType
import es.jocasolo.competitiveeventsapp.enums.score.ScoreValueType
import es.jocasolo.competitiveeventsapp.fragment.participant.ParticipantsEditDialogFragment
import es.jocasolo.competitiveeventsapp.fragment.participant.ParticipantsListFragment
import es.jocasolo.competitiveeventsapp.singleton.UserAccount
import java.time.Duration


open class ListParticipantsAdapter(
    var context: Context,
    var listFragment: ParticipantsListFragment,
    var participants: List<ParticipantDTO>,
    var eventId: String,
    var scoreValueType: ScoreValueType,
    var isAdmin: Boolean
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
                holder.itemName.text = participant.id
                holder.itemStatus.text = getPrivilege(participant.privilege)
                if(participant.status != EventUserStatusType.ACCEPTED) {
                    holder.itemStatus.text = getStatus(participant.status)
                    holder.itemStatus.setTextColor(context.resources.getColor(R.color.accent))
                }
                holder.itemScore.text = getScoreValue(participant.score)
                if(isAdmin){
                    holder.itemEdit.visibility = View.VISIBLE
                    holder.itemEdit.setOnClickListener {
                        openParticipantActionsDialog(participant) }
                }
                if(participant.id.equals(UserAccount.getInstance(context).getName())){
                    holder.itemEdit.visibility = View.GONE
                }
                if(participant.avatar != null) {
                    holder.itemImage.imageTintMode = null
                    Picasso.get()
                        .load(participant.avatar)
                        .resize(100, 100)
                        .centerCrop()
                        .error(R.drawable.military_tech)
                        .into(holder.itemImage)
                }
            }
        }
    }

    private fun getStatus(status: EventUserStatusType?): String? {
        return when(status) {
            EventUserStatusType.ACCEPTED -> context.getString(R.string.user_accepted)
            EventUserStatusType.DELETED -> context.getString(R.string.user_deleted)
            EventUserStatusType.REJECTED -> context.getString(R.string.user_reject)
            EventUserStatusType.WAITING_APPROVAL -> context.getString(R.string.user_waiting_approval)
            EventUserStatusType.INVITED -> context.getString(R.string.user_invited)
            else -> {
                context.getString(R.string.user_accepted)
            }
        }
    }

    private fun openParticipantActionsDialog(participant: ParticipantDTO) {
        val ft: FragmentTransaction = listFragment.parentFragmentManager.beginTransaction()
        val prev = listFragment.parentFragmentManager.findFragmentByTag("dialogParticipants")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newDialogFragment: ParticipantsEditDialogFragment = ParticipantsEditDialogFragment(listFragment, participant, eventId)
        newDialogFragment.show(ft, "dialogParticipants")
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

    private fun getPrivilege(privilege: EventUserPrivilegeType?): String {
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
        var itemStatus: TextView = itemView.findViewById(R.id.txt_participant_status)

    }

}