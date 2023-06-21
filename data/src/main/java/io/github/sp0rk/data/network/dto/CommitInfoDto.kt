package io.github.sp0rk.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CommitInfoDto(
    val author: AuthorDto,
    val message: String,
)
