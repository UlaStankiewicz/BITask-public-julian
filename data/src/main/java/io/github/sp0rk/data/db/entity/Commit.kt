package io.github.sp0rk.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Commit(
    @PrimaryKey val sha: String,
    val author: String,
    val date: String,
    val message: String,
    val repository_id: Long,
)
