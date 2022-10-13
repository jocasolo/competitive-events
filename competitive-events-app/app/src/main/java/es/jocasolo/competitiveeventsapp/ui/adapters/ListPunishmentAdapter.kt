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
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentDTO

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
                holder.itemRequiredPosition.text = context.getString(R.string.reward_required_position, punishment.requiredPosition)
                if(punishment.image != null) {
                    holder.itemImage.imageTintMode = null
                    Picasso.get()
                            .load(punishment.image?.link())
                            .resize(100, 100)
                            .centerCrop()
                            .error(R.drawable.military_tech)
                            .into(holder.itemImage)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        punishments.let {
            return punishments.size
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView = itemView.findViewById(R.id.img_punishment_item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.txt_punishment_item_title)
        var itemDescription: TextView = itemView.findViewById(R.id.txt_punishment_item_description)
        var itemLooser: TextView = itemView.findViewById(R.id.txt_punishment_item_looser)
        var itemRequiredPosition: TextView = itemView.findViewById(R.id.txt_punishment_item_required_position)

    }

}