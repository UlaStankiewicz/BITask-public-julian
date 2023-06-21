package io.github.sp0rk.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommitRemoteKeys(
    @PrimaryKey
    val sha: String,
    val repositoryId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
