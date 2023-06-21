package io.github.sp0rk.bitask.feature.repository.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.sp0rk.data.db.entity.Repository
import io.github.sp0rk.bitask.databinding.ItemRepositoryBinding

class RepositoryListAdapter(
    private val onItemClicked: (Repository) -> Unit,
) : ListAdapter<Repository, RepositoryViewHolder>(RepositoryDiffCallback()) {

    override fun getItemId(position: Int) = getItem(position).path.hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RepositoryViewHolder(
        ItemRepositoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item = item,
            onItemClicked = onItemClicked,
        )
    }
}

private class RepositoryDiffCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Repository, newItem: Repository) = oldItem == newItem
}
