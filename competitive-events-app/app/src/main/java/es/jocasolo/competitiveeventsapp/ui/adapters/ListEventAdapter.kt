package es.jocasolo.competitiveeventsapp.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO
import es.jocasolo.competitiveeventsapp.utils.MyUtils

open class ListEventAdapter(var navController: NavController, var eventsPage: EventPageDTO, val listType: ListEventType): RecyclerView.Adapter<ListEventAdapter.ViewHolder>() {

    enum class ListEventType {
        HOME, SEARCH
    }

    private var seconds: String? = null
    private var minutes: String? = null
    private var hours: String? = null
    private var days: String? = null
    private var parent : ViewGroup? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.card_layout_event,
                parent,
                false
        )

        this.parent = parent

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(eventsPage.events != null && eventsPage.events!!.size > position) {
            val event = eventsPage.events?.get(position)
            event?.let {
                holder.card.setOnClickListener { openDetail(event) }
                holder.itemTitle.text = event.title.toString()
                holder.itemSubtitle.text = event.subtitle.toString()
                holder.itemParticipants.text = event.numParticipants.toString()
                Picasso.get()
                        .load(event.image?.link())
                        .resize(65, 65)
                        .centerCrop()
                        .error(R.drawable.rugby)
                        .into(holder.itemImage)
                val time = MyUtils.getTimeToFinish(parent!!.context, event.endDate)
                if (time.isNotEmpty()) {
                    holder.itemClock.text = time
                } else {
                    holder.itemClock.visibility = View.INVISIBLE
                    holder.imgClock.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun openDetail(event: EventDTO) {

        val data : Bundle = Bundle()
        data.putString("eventId", event.id)

        navController.navigate(R.id.action_event_search_to_event_detail, data)
    }

    override fun getItemCount(): Int {
        eventsPage.total?.let {
            return eventsPage.total!!.toInt()
        }
        return 0;
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView = itemView.findViewById(R.id.img_event_item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.txt_event_item_title)
        var itemSubtitle: TextView = itemView.findViewById(R.id.txt_event_item_subtitle)
        var itemParticipants: TextView = itemView.findViewById(R.id.txt_item_event_participants)
        var itemClock: TextView = itemView.findViewById(R.id.txt_item_event_clock)
        var imgClock: ImageView = itemView.findViewById(R.id.img_item_event_clock)
        var card: CardView = itemView.findViewById(R.id.card_event)

    }

}