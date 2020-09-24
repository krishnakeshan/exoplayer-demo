package com.qrilt.trell_demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qrilt.trell_demo.R
import com.qrilt.trell_demo.model.UserVideo

class VideosAdapter(
    private var videos: List<UserVideo>,
    private var listener: InteractionListener
) :
    RecyclerView.Adapter<VideosAdapter.UserVideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_user_video, parent, false)
        val userVideoViewHolder = UserVideoViewHolder(view)
        return UserVideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserVideoViewHolder, position: Int) {
        holder.nameTextView.text = videos[position].name
        holder.nameTextView.setOnClickListener(View.OnClickListener {
            listener.onItemSelected(position)
        })
    }

    override fun getItemCount(): Int = videos.size

    fun setContent(userVideos: List<UserVideo>) {
        this.videos = userVideos
    }

    // View Holder
    class UserVideoViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        // Properties
        val nameTextView: TextView = view.findViewById(R.id.view_holder_user_video_name_text_view)
    }

    interface InteractionListener {
        fun onItemSelected(position: Int)
    }
}