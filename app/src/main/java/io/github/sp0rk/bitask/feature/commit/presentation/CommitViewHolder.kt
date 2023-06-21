package io.github.sp0rk.bitask.feature.commit.presentation

import androidx.recyclerview.widget.RecyclerView
import io.github.sp0rk.bitask.R
import io.github.sp0rk.bitask.databinding.ItemCommitBinding
import io.github.sp0rk.bitask.feature.commit.presentation.model.CommitListItem

class CommitViewHolder(
    private val binding: ItemCommitBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: CommitListItem?,
        onItemClicked: (CommitListItem) -> Unit,
    ) = with(binding) {
        item?.commit?.let { commit ->
            sha.text = commit.sha
            author.text = commit.author
            message.text = commit.message

            root.setOnClickListener {
                onItemClicked(item)
            }

            select(item.selected)
        }
    }

    fun select(selected: Boolean) = with(binding) {
        card.setCardBackgroundColor(
            card.context.getColor(
                if (selected)
                    R.color.selected
                else
                    R.color.white
            )
        )
    }
}
