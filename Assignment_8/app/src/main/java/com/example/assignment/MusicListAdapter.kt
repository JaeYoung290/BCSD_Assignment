package com.example.assignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicListAdapter(private val musicList: List<MusicItem>) :
    RecyclerView.Adapter<MusicListAdapter.MusicViewHolder>() {
    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        val artistTextView: TextView = itemView.findViewById(R.id.artist_text_view)
        val durationTextView: TextView = itemView.findViewById(R.id.duration_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music_list, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MusicViewHolder, position: Int) {
        viewHolder.titleTextView.text = musicList[position].title
        viewHolder.artistTextView.text = musicList[position].artist
        viewHolder.durationTextView.text = musicList[position].duratin
    }

    override fun getItemCount() = musicList.size
}