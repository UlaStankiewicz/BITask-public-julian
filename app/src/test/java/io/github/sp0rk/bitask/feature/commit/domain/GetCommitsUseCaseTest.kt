package io.github.sp0rk.bitask.feature.commit.domain

import io.github.sp0rk.data.network.GithubApi
import io.github.sp0rk.data.network.dto.CommitDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetCommitsUseCaseTest {
    private val invalidPath = "sp0rk"
    private val path = "sp0rk/spork"
    private val page = 1
    private val pageSize = 20
    private val response = Response.success(emptyList<CommitDto>())

    private val api = mockk<GithubApi> {
        coEvery { getCommits("sp0rk", "spork", page, pageSize) } returns response
    }

    private fun createSutForPath(path: String) = GetCommitsUseCase { _, _, _ ->
        getCommits(api, path, page, pageSize)
    }

    @Test
    fun `WHEN getCommits THEN call api`() = runTest {
        val sut = createSutForPath(path)
        val result = sut(path, page, pageSize)

        assertEquals(response, result)
    }

    @Test
    fun `GIVEN path is invalid WHEN getCommits THEN call api`() = runTest {
        val sut = createSutForPath(invalidPath)

        assertFailsWith<IndexOutOfBoundsException> {
            sut(invalidPath, page, pageSize)
        }
    }
}
