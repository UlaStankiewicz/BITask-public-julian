package io.github.sp0rk.bitask.feature.commit.domain

import io.github.sp0rk.data.network.GithubApi
import io.github.sp0rk.data.network.dto.CommitDto
import retrofit2.Response

fun interface GetCommitsUseCase : suspend (String, Int, Int) -> Response<List<CommitDto>>

suspend fun getCommits(
    api: GithubApi,
    repositoryPath: String,
    page: Int,
    pageSize: Int
): Response<List<CommitDto>> {
    val (owner, repo) = repositoryPath.split("/")
    return api.getCommits(owner, repo, page, pageSize)
}
