package io.github.sp0rk.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.sp0rk.data.db.dao.CommitDao
import io.github.sp0rk.data.db.dao.CommitRemoteKeyDao
import io.github.sp0rk.data.db.dao.RepositoryDao
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.CommitRemoteKeys
import io.github.sp0rk.data.db.entity.Repository

@Database(entities = [Repository::class, Commit::class, CommitRemoteKeys::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
    abstract fun commitDao(): CommitDao
    abstract fun commitRemoteKeysDao(): CommitRemoteKeyDao
}
