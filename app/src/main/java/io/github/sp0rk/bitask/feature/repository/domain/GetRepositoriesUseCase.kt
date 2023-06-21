package io.github.sp0rk.bitask.feature.repository.domain

import io.github.sp0rk.data.db.dao.RepositoryDao
import io.github.sp0rk.data.db.entity.Repository
import kotlinx.coroutines.flow.Flow

fun interface GetRepositoriesUseCase : () -> Flow<List<Repository>>

fun getRepositories(repositoryDao: RepositoryDao): Flow<List<Repository>> =
    repositoryDao.getAll()
