package io.github.sp0rk.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    val name: String,
    val date: String,
)
