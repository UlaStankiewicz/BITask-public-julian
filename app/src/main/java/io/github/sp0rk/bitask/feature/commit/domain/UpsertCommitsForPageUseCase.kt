package io.github.sp0rk.bitask.feature.commit.domain

import androidx.room.withTransaction
import io.github.sp0rk.data.db.LocalDatabase
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.CommitRemoteKeys
import io.github.sp0rk.data.db.entity.Repository

fun interface UpsertCommitsForPageUseCase :
    suspend (List<Commit>, Int, Int, Boolean, Long) -> Boolean

/**
 * @return whether the end of pagination was reached
 */
suspend fun upsertCommitsForPage(
    db: LocalDatabase,
    commits: List<Commit>,
    pageSize: Int,
    page: Int,
    forceRefresh: Boolean,
    repositoryId: Long,
): Boolean {
    val endOfPaginationReached = commits.size < pageSize

    db.withTransaction {
        if (forceRefresh) {
            db.commitDao().clearFromRepository(repositoryId)
            db.commitRemoteKeysDao().clearRemoteKeys(repositoryId)
        }
        val prevKey = if (page == 0) null else page - 1
        val nextKey = if (endOfPaginationReached) null else page + 1
        val keys = commits.map {
            CommitRemoteKeys(
                sha = it.sha, repositoryId = repositoryId, prevKey = prevKey, nextKey = nextKey
            )
        }
        db.commitRemoteKeysDao().insertAll(keys)
        db.commitDao().insertAll(commits)
    }
    return endOfPaginationReached
}
