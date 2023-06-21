package io.github.sp0rk.data.network.dto

import io.github.sp0rk.data.db.entity.Commit
import kotlinx.serialization.Serializable

@Serializable
data class CommitDto(
    val sha: String,
    val commit: CommitInfoDto,
) {
    fun toEntity(repositoryId: Long) = Commit(
        sha = sha,
        author = commit.author.name,
        date = commit.author.date,
        message = commit.message,
        repository_id = repositoryId
    )
}

