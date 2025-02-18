package com.example.assignment.presemtation.main.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.data.model.GitHubRepo
import com.example.assignment.databinding.ItemRepoBinding

class RepoAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<GitHubRepo, RepoAdapter.RepoViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GitHubRepo>() {
            override fun areItemsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoAdapter.RepoViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoAdapter.RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RepoViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: GitHubRepo) {
            binding.repo = repo
            binding.root.setOnClickListener {
                onItemClick(repo.html_url)
            }
        }
    }
}