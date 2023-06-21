package io.github.sp0rk.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.sp0rk.data.db.entity.Commit

@Dao
interface CommitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(commits: List<Commit>)

    @Query("SELECT * FROM `commit` WHERE repository_id = :repositoryId ORDER BY date DESC")
    fun pagingSource(repositoryId: Long): PagingSource<Int, Commit>

    @Query("DELETE FROM `commit`  WHERE repository_id = :repositoryId")
    suspend fun clearFromRepository(repositoryId: Long)
}
