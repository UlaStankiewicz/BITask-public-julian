package io.github.sp0rk.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.sp0rk.data.db.entity.CommitRemoteKeys

@Dao
interface CommitRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CommitRemoteKeys>)

    @Query("SELECT * FROM CommitRemoteKeys WHERE sha = :sha")
    suspend fun remoteKeysBySha(sha: String): CommitRemoteKeys?

    @Query("DELETE FROM CommitRemoteKeys WHERE repositoryId = :repositoryId")
    suspend fun clearRemoteKeys(repositoryId: Long)
}
