package io.github.sp0rk.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Repository(
    @PrimaryKey val id: Long,
    val path: String,
) : java.io.Serializable
