package io.github.sp0rk.bitask.feature.commit.presentation.model

import io.github.sp0rk.data.db.entity.Commit

data class CommitListItem(
    val commit: Commit,
    var selected: Boolean
)
