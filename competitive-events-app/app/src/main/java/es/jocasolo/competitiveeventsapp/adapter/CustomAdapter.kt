package es.jocasolo.competitiveeventsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.jocasolo.competitiveeventsapp.R
import es.jocasolo.competitiveeventsapp.dto.event.EventPageDTO

class CustomAdapter(var eventsPage: EventPageDTO): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_event, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventsPage.events?.get(position)
        holder.itemTitle.text = event?.title.toString()
        holder.itemSubtitle.text = event?.subtitle.toString()
        Picasso.get()
                .load(event?.image?.url)
                .resize(65, 65)
                .centerCrop()
                .error(R.drawable.rugby)
                .into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        eventsPage.total?.let {
            return eventsPage.total!!.toInt()
        }
        return 0;
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView
        var itemTitle: TextView
        var itemSubtitle: TextView

        init {
            itemImage = itemView.findViewById(R.id.img_event_item_image);
            itemTitle = itemView.findViewById(R.id.txt_event_item_title);
            itemSubtitle = itemView.findViewById(R.id.txt_event_item_subtitle);
        }

    }

}