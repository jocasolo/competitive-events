package es.jocasolo.competitiveeventsapp.ui.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.EventActivity
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.event.EventDTO
import es.jocasolo.competitiveeventsapp.utils.MyUtils


open class ListEventAdapter(
    var fragment: Fragment,
    var events: MutableList<EventDTO>?,
    var type: ListEventType
): RecyclerView.Adapter<ListEventAdapter.ViewHolder>() {

    enum class ListEventType {
        HOME, SEARCH, INVITATIONS
    }

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
        if(events != null && events!!.size > position) {
            val event = events?.get(position)
            event?.let {
                holder.card.setOnClickListener { openDetail(event) }
                holder.itemTitle.text = event.title.toString()
                holder.itemSubtitle.text = event.subtitle.toString()
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
                if(type == ListEventType.INVITATIONS) {
                    holder.itemInvited.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openDetail(event: EventDTO) {

        when(type) {
            ListEventType.HOME -> {
                val myIntent = Intent(fragment.requireActivity(), EventActivity::class.java)
                myIntent.putExtra("eventId", event.id)
                myIntent.putExtra("eventTitle", event.title)
                fragment.requireActivity().startActivity(myIntent)
            }
            ListEventType.SEARCH -> {
                val data : Bundle = Bundle()
                data.putString("eventId", event.id)
                fragment.findNavController().navigate(R.id.action_event_search_to_event_detail, data)
            }
            ListEventType.INVITATIONS -> {
                val data : Bundle = Bundle()
                data.putString("eventId", event.id)
                data.putBoolean("showRejectButton", true)
                fragment.findNavController().navigate(R.id.action_home_to_event_detail, data)
            }
        }

    }

    override fun getItemCount(): Int {
        if(!events.isNullOrEmpty()){
            return events!!.size
        }
        return 0;
    }

    fun addEvents(newEvents: List<EventDTO>){
        if(events == null) {
            events = mutableListOf()
        }
        events?.addAll(newEvents)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView = itemView.findViewById(R.id.img_event_item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.txt_event_item_title)
        var itemSubtitle: TextView = itemView.findViewById(R.id.txt_event_item_subtitle)
        var itemClock: TextView = itemView.findViewById(R.id.txt_item_event_clock)
        var imgClock: ImageView = itemView.findViewById(R.id.img_item_event_clock)
        var card: CardView = itemView.findViewById(R.id.card_event)
        var itemInvited: TextView = itemView.findViewById(R.id.txt_user_invited)

    }

}