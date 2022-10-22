package es.jocasolo.competitiveeventsapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.BackStackEntryDTO
import es.jocasolo.competitiveeventsapp.dto.reward.RewardPostDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType

open class ListRewardLiteAdapter(
    var context : Context,
    var rewards: MutableList<BackStackEntryDTO>?,
    var showEditButton : Boolean = false
): RecyclerView.Adapter<ListRewardLiteAdapter.ViewHolder>() {

    var rewardsToDelete : MutableList<Int> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.card_layout_reward_lite,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(rewards != null && rewards!!.size > position) {
            val reward = rewards?.get(position) as RewardPostDTO
            reward.let { r ->
                holder.itemTitle.text = reward.title
                val sort = context.getString(getSortRequiredPosition(reward))
                holder.itemRequiredPosition.text = context.getString(R.string.reward_required_position, reward.requiredPosition, sort)
                if(showEditButton){
                    holder.btnEdit.visibility = View.VISIBLE
                    holder.btnEdit.setOnClickListener {
                        rewardsToDelete.add(r.id!!)
                        rewards?.remove(reward)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun addReward(newReward : BackStackEntryDTO?){
        if(rewards == null) {
            rewards = mutableListOf()
        }
        if (newReward != null) {
            rewards?.add(newReward)
        }
    }

    override fun getItemCount(): Int {
        if(!rewards.isNullOrEmpty()){
            return rewards!!.size
        }
        return 0
    }

    private fun getSortRequiredPosition(reward: BackStackEntryDTO?): Int {
        if(reward is RewardPostDTO) {
            reward.sortScore?.let {
                if (it == ScoreSortType.ASC)
                    return R.string.higher
            }
        }
        return R.string.lower
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemTitle: TextView = itemView.findViewById(R.id.txt_reward_lite_item_title)
        var itemRequiredPosition: TextView = itemView.findViewById(R.id.txt_reward_lite_item_required_position)
        var btnEdit : ImageView = itemView.findViewById(R.id.img_reward_delete)

    }

}