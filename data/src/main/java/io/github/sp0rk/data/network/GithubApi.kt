package io.github.sp0rk.data.network

import io.github.sp0rk.data.network.dto.CommitDto
import io.github.sp0rk.data.network.dto.RepositoryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("repos/{owner}/{repo}/commits")
    suspend fun getCommits(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
    ): Response<List<CommitDto>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<RepositoryDto>
}
