package com.example.assignment.feature_word.presentation.words.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.assignment.databinding.ItemWordBinding
import com.example.assignment.feature_word.domain.model.Word

class WordAdapter : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private var words = emptyList<Word>()
    private var onDeleteClickListener: ((Word) -> Unit)? = null
    private var onItemClickListener: ((Word) -> Unit)? = null

    inner class WordViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.word = word
            word.imageUri?.let { uri ->
                Glide.with(itemView.context)
                    .load(uri)
                    .into(binding.ivItemImage)
            }
            binding.executePendingBindings()

            itemView.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(word)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WordViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.bind(word)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newWords: List<Word>) {
        words = newWords
        notifyDataSetChanged()
    }

    fun setOnDeleteClickListener(listener: (Word) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnItemClickListener(listener: (Word) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        @JvmStatic
        @BindingAdapter("items")
        fun setItems(recyclerView: RecyclerView, items: List<Word>?) {
            items?.let {
                (recyclerView.adapter as? WordAdapter)?.setData(it)
            }
        }
    }
}