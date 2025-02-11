package com.example.assignment.feature_word.presentation.words.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test_1.databinding.ItemWordBinding
import com.example.assignment.feature_word.domain.model.Word

class WordAdapter : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private var words = emptyList<Word>()
    private var onDeleteClickListener: ((Word) -> Unit)? = null
    private var onItemClickListener: ((Word) -> Unit)? = null

    inner class WordViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.apply {
                tvItemWord.text = word.word
                tvItemWordMeaning.text = word.meaning

                root.setOnClickListener {
                    onItemClickListener?.invoke(word)
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
        this.words = newWords
        notifyDataSetChanged()
    }

    fun setOnDeleteClickListener(listener: (Word) -> Unit) {
        onDeleteClickListener = listener
    }

    fun setOnItemClickListener(listener: (Word) -> Unit) {
        onItemClickListener = listener
    }
}