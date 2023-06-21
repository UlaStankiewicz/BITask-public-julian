package io.github.sp0rk.data.network.dto

import io.github.sp0rk.data.db.entity.Repository
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryDto(
    val id: Long,
    val full_name: String,
) {
    fun toEntity() = Repository(id, full_name)
}
