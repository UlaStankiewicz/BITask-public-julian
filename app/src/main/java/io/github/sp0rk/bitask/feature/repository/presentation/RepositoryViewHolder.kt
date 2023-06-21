package io.github.sp0rk.bitask.feature.repository.presentation

import androidx.recyclerview.widget.RecyclerView
import io.github.sp0rk.bitask.databinding.ItemRepositoryBinding
import io.github.sp0rk.data.db.entity.Repository

class RepositoryViewHolder(
    private val binding: ItemRepositoryBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: Repository,
        onItemClicked: (Repository) -> Unit,
    ) = with(binding) {
        path.text = item.path
        repoId.text = item.id.toString()
        root.setOnClickListener { onItemClicked(item) }
    }
}
