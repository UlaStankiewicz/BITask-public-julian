package io.github.sp0rk.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.sp0rk.data.db.entity.Repository
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg repositories: Repository)

    @Query("SELECT * FROM repository")
    fun getAll(): Flow<List<Repository>>

    @Query("SELECT * FROM repository WHERE path = :path")
    suspend fun get(path: String): Repository?
}
