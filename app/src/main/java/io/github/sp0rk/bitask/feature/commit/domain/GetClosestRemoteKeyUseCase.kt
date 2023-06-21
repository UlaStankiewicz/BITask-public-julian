package io.github.sp0rk.bitask.feature.commit.domain

import androidx.paging.PagingState
import androidx.room.withTransaction
import io.github.sp0rk.data.db.LocalDatabase
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.CommitRemoteKeys

fun interface GetClosestRemoteKeyUseCase : suspend (PagingState<Int, Commit>) -> CommitRemoteKeys?

suspend fun getClosestRemoteKey(
    db: LocalDatabase,
    state: PagingState<Int, Commit>
): CommitRemoteKeys? = state.anchorPosition?.let { position ->
    state.closestItemToPosition(position)?.sha?.let { sha ->
        db.withTransaction { db.commitRemoteKeysDao().remoteKeysBySha(sha) }
    }
}
