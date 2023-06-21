package io.github.sp0rk.bitask.feature.commit.domain

import io.github.sp0rk.data.db.dao.CommitDao

class CommitPagingSourceFactory(private val commitDao: CommitDao) {
    operator fun invoke(repositoryId: Long) = commitDao.pagingSource(repositoryId)
}
