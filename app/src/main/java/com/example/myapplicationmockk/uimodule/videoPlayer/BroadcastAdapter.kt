package com.viewlift.uimodule.videoPlayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.viewlift.uimodule.R
import com.vl.viewlift.playersdk.model.BroadcastData

class BroadcastAdapter(private val mList: List<BroadcastData>,private val broadcastViewType: BroadcastViewType) :
    RecyclerView.Adapter<BroadcastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(if (broadcastViewType == BroadcastViewType.HORIZONTAL) R.layout.broadcast_item else R.layout.broadcast_item_vertical, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbImage: ImageView = itemView.findViewById(com.viewlift.uimodule.R.id.air_play_button)
        fun bind(broadcastData: BroadcastData) {
            Picasso.get()
                .load(broadcastData.thumbnail)
//                .error(R.drawable.video_placeholder)
//                .placeholder(R.drawable.video_placeholder)
                .into(thumbImage)
        }
    }

    enum class BroadcastViewType {
        HORIZONTAL,
        VERTICAL
    }
}