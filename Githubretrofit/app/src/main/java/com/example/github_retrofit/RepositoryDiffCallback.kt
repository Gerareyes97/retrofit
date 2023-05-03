package com.example.github_retrofit

import androidx.recyclerview.widget.DiffUtil

class RepositoryDiffCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.name == newItem.name && oldItem.description == newItem.description
    }
}

