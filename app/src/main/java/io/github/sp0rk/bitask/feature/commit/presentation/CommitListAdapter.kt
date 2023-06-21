package io.github.sp0rk.bitask.feature.commit.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import io.github.sp0rk.bitask.databinding.ItemCommitBinding
import io.github.sp0rk.bitask.feature.commit.presentation.model.CommitListItem

class CommitListAdapter(
    private val onItemClicked: (CommitListItem) -> Boolean,
) : PagingDataAdapter<CommitListItem, CommitViewHolder>(CommitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommitViewHolder(
        ItemCommitBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item = item,
            onItemClicked = {
                val selected = onItemClicked(it)
                item?.selected = selected
                holder.select(selected)
            }
        )
    }
}

private class CommitDiffCallback : DiffUtil.ItemCallback<CommitListItem>() {
    override fun areItemsTheSame(oldItem: CommitListItem, newItem: CommitListItem) =
        oldItem.commit == newItem.commit

    override fun areContentsTheSame(oldItem: CommitListItem, newItem: CommitListItem) =
        oldItem.commit == newItem.commit
}
