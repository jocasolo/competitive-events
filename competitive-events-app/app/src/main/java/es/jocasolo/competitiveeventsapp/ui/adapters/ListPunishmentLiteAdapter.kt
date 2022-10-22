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
import es.jocasolo.competitiveeventsapp.dto.punishment.PunishmentPostDTO
import es.jocasolo.competitiveeventsapp.enums.score.ScoreSortType

open class ListPunishmentLiteAdapter(
    var context : Context,
    var punishments: MutableList<BackStackEntryDTO>?,
    var showEditButton : Boolean = false
): RecyclerView.Adapter<ListPunishmentLiteAdapter.ViewHolder>() {

    var punishmentsToDelete : MutableList<Int> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.card_layout_punishment_lite,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(punishments != null && punishments!!.size > position) {
            val punishment = punishments?.get(position) as PunishmentPostDTO
            punishment.let { p ->
                holder.itemTitle.text = punishment.title
                val sort = context.getString(getSortRequiredPosition(punishment))
                holder.itemRequiredPosition.text = context.getString(R.string.reward_required_position, punishment.requiredPosition, sort)
                if(showEditButton){
                    holder.btnEdit.visibility = View.VISIBLE
                    holder.btnEdit.setOnClickListener {
                        punishmentsToDelete.add(p.id!!)
                        punishments?.remove(p)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun addPunishment(newPunishment : BackStackEntryDTO?){
        if(punishments == null) {
            punishments = mutableListOf()
        }
        if (newPunishment != null) {
            punishments?.add(newPunishment)
        }
    }

    override fun getItemCount(): Int {
        if(!punishments.isNullOrEmpty()){
            return punishments!!.size
        }
        return 0
    }

    private fun getSortRequiredPosition(punishment: BackStackEntryDTO?): Int {
        if(punishment is PunishmentPostDTO) {
            punishment.sortScore?.let {
                if (it == ScoreSortType.ASC)
                    return R.string.higher
            }
        }
        return R.string.lower
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemTitle: TextView = itemView.findViewById(R.id.txt_punishment_lite_item_title)
        var itemRequiredPosition: TextView = itemView.findViewById(R.id.txt_punishment_lite_item_required_position)
        var btnEdit : ImageView = itemView.findViewById(R.id.img_punishment_delete)

    }

}