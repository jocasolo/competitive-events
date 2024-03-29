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
import es.jocasolo.competitiveeventsapp.dto.reward.RewardDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType
import es.jocasolo.competitiveeventsapp.utils.MyUtils

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
                val sort = context.getString(getSortRequiredPosition(reward))
                holder.itemRequiredPosition.text = context.getString(R.string.reward_required_position, reward.requiredPosition, sort)
                reward.winner?.let {
                    holder.itemWinner.visibility = View.VISIBLE
                    holder.itemWinner.text = context.getString(R.string.reward_winner, reward.winner!!.id)
                }
                if(reward.image != null) {
                    holder.itemImage.imageTintMode = null
                    Picasso.get()
                            .load(reward.image?.link())
                            .resize(100, 100)
                            .centerCrop()
                            .error(R.drawable.military_tech)
                            .into(holder.itemImage)
                    holder.itemImage.setOnClickListener { MyUtils.zoomToThisImage(context, reward.image!!) }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        rewards.let {
            return rewards.size
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

        var itemImage: ImageView = itemView.findViewById(R.id.img_reward_item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.txt_reward_item_title)
        var itemDescription: ReadMoreTextView = itemView.findViewById(R.id.txt_reward_item_description)
        var itemWinner: TextView = itemView.findViewById(R.id.txt_reward_item_winner)
        var itemRequiredPosition: TextView = itemView.findViewById(R.id.txt_reward_item_required_position)

    }

}