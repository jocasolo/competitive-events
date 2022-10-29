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
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.utils.MyUtils

class ListPunishmentAdapter (var context : Context, var punishments: List<PunishmentDTO>): RecyclerView.Adapter<ListPunishmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.card_layout_punishment,
                parent,
                false
        )

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(punishments.size > position) {
            val punishment = punishments[position]
            punishment.let {
                holder.itemTitle.text = punishment.title
                holder.itemDescription.text = punishment.description.toString()
                punishment.looser?.let {
                    holder.itemLooser.visibility = View.VISIBLE
                    holder.itemLooser.text = context.getString(R.string.reward_winner, punishment.looser!!.id)
                }
                val sort = context.getString(getSortRequiredPosition(punishment))
                holder.itemRequiredPosition.text = context.getString(R.string.reward_required_position, punishment.requiredPosition, sort)
                if(punishment.image != null) {
                    holder.itemImage.imageTintMode = null
                    Picasso.get()
                            .load(punishment.image?.link())
                            .resize(100, 100)
                            .centerCrop()
                            .error(R.drawable.military_tech)
                            .into(holder.itemImage)
                    holder.itemImage.setOnClickListener { MyUtils.zoomToThisImage(context, punishment.image!!) }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        punishments.let {
            return punishments.size
        }
    }

    private fun getSortRequiredPosition(punishment: PunishmentDTO): Int {
        punishment.sortScore?.let {
            if(it == ScoreSortType.ASC)
                return R.string.higher
        }
        return R.string.lower
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView = itemView.findViewById(R.id.img_punishment_item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.txt_punishment_item_title)
        var itemDescription: ReadMoreTextView = itemView.findViewById(R.id.txt_punishment_item_description)
        var itemLooser: TextView = itemView.findViewById(R.id.txt_punishment_item_looser)
        var itemRequiredPosition: TextView = itemView.findViewById(R.id.txt_punishment_item_required_position)

    }

}