package com.example.assignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.databinding.ItemLapBinding

class LapAdapter(private val lapTimes: MutableList<String>) : RecyclerView.Adapter<LapAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemLapBinding) : RecyclerView.ViewHolder(binding.root) {
        val lapTimeTextView = binding.lapTimeTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.lapTimeTextView.text = lapTimes[position]
    }

    override fun getItemCount() = lapTimes.size
}