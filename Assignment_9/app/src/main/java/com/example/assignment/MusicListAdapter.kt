package com.example.assignment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.databinding.ItemMusicListBinding

class MusicListAdapter(
    private val musicList: List<MusicItem>,
    private val onItemClick: (MusicItem) -> Unit
) : RecyclerView.Adapter<MusicListAdapter.MusicViewHolder>() {

    class MusicViewHolder(private val binding: ItemMusicListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(musicItem: MusicItem, onItemClick: (MusicItem) -> Unit) {
            binding.titleTextView.text = musicItem.title
            binding.artistTextView.text = musicItem.artist
            binding.durationTextView.text = formatDuration(musicItem.duration.toLong())

            itemView.setOnClickListener {
                onItemClick(musicItem)
            }
        }

        @SuppressLint("DefaultLocale")
        private fun formatDuration(durationMs: Long): String {
            val seconds = durationMs / 1000 % 60
            val minutes = durationMs / 1000 / 60 % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemMusicListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val musicItem = musicList[position]
        holder.bind(musicItem, onItemClick)
    }

    override fun getItemCount() = musicList.size

}