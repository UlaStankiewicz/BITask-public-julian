package io.github.sp0rk.bitask.feature.search.domain

import io.github.sp0rk.data.db.dao.RepositoryDao
import io.github.sp0rk.data.db.entity.Repository
import io.github.sp0rk.data.network.GithubApi
import io.github.sp0rk.bitask.feature.commit.domain.model.Result
import java.io.IOException

fun interface FindRepositoryUseCase : suspend (String) -> Result<Repository>

suspend fun findRepository(
    githubApi: GithubApi,
    repositoryDao: RepositoryDao,
    repositoryPath: String
): Result<Repository> = repositoryDao.get(repositoryPath)
    ?.let { Result.Success(it) }
    ?: try {
        val (owner, repo) = repositoryPath.split("/")
        val response = githubApi.getRepository(owner, repo)
        response.body()?.let { dto ->
            val repository = dto.toEntity()
            repositoryDao.insertAll(repository)
            Result.Success(repository)
        } ?: Result.Failure(IOException(response.code().toString()))
    } catch (e: Exception) {
        Result.Failure(e)
    }
