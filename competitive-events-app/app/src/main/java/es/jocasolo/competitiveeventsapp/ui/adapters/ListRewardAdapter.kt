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
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO

open class ListRewardAdapter(var context : Context, var rewards: List<RewardDTO>): RecyclerView.Adapter<ListRewardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.card_layout_reward,
            parent,
            false
        )

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(rewards.size > position) {
            val reward = rewards[position]
            reward.let {
                holder.itemTitle.text = reward.title
                holder.itemDescription.text = reward.description.toString()
                reward.winner?.let {
                    holder.itemWinner.visibility = View.VISIBLE
                    holder.itemWinner.text = context.getString(R.string.reward_winner, reward.winner!!.id)
                }
                holder.itemRequiredPosition.text = context.getString(R.string.reward_required_position, reward.requiredPosition)
                if(reward.image != null) {
                    holder.itemImage.imageTintMode = null
                    Picasso.get()
                            .load(reward.image?.link())
                            .resize(100, 100)
                            .centerCrop()
                            .error(R.drawable.military_tech)
                            .into(holder.itemImage)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        rewards.let {
            return rewards.size
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView = itemView.findViewById(R.id.img_reward_item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.txt_reward_item_title)
        var itemDescription: TextView = itemView.findViewById(R.id.txt_reward_item_description)
        var itemWinner: TextView = itemView.findViewById(R.id.txt_reward_item_winner)
        var itemRequiredPosition: TextView = itemView.findViewById(R.id.txt_reward_item_required_position)

    }

}