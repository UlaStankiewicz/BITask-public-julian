package io.github.sp0rk.bitask.feature.commit.domain

import androidx.paging.PagingState
import androidx.room.withTransaction
import io.github.sp0rk.data.db.LocalDatabase
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.CommitRemoteKeys

fun interface GetRemoteKeyForLastItemUseCase :
    suspend (PagingState<Int, Commit>) -> CommitRemoteKeys?

suspend fun getRemoteKeyForLastItem(
    db: LocalDatabase,
    state: PagingState<Int, Commit>
): CommitRemoteKeys? = state.lastItemOrNull()?.let { commit ->
    db.withTransaction { db.commitRemoteKeysDao().remoteKeysBySha(commit.sha) }
}
