package io.github.sp0rk.bitask.feature.search.domain

import io.github.sp0rk.bitask.feature.commit.domain.model.Result
import io.github.sp0rk.data.db.dao.RepositoryDao
import io.github.sp0rk.data.db.entity.Repository
import io.github.sp0rk.data.network.GithubApi
import io.github.sp0rk.data.network.dto.RepositoryDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.test.assertEquals
import kotlin.test.assertIs

class FindRepositoryUseCaseTest {
    private val api = mockk<GithubApi>()
    private val dao = mockk<RepositoryDao> {
        coEvery { insertAll(any()) } just runs
    }
    private val invalidPath = "sp0rk"
    private val path = "sp0rk/spork"
    private val repository = Repository(0L, path)
    private val dto = RepositoryDto(0L, path)
    private val responseSuccess = Response.success(200, dto)
    private val responseFailure = mockk<Response<RepositoryDto>> {
        every { body() } returns null
        every { code() } returns 404
    }

    private fun createSutForPath(path: String) = FindRepositoryUseCase {
        findRepository(api, dao, path)
    }

    @Test
    fun `GIVEN local copy present WHEN find repository THEN return local copy`() = runTest {
        coEvery { dao.get(path) } returns repository
        val sut = createSutForPath(path)

        val result = sut(path)

        assertEquals(result, Result.Success(repository))
    }

    @Test
    fun `GIVEN local copy missing AND path invalid WHEN find repository THEN fail`() = runTest {
        coEvery { dao.get(invalidPath) } returns null
        val sut = createSutForPath(invalidPath)

        val result = sut(invalidPath)

        assertIs<IndexOutOfBoundsException>((result as Result.Failure).error)
    }

    @Test
    fun `GIVEN local copy missing AND api call throws WHEN find repository THEN fail`() = runTest {
        coEvery { dao.get(path) } returns null
        coEvery { api.getRepository(any(), any()) } throws SocketTimeoutException()
        val sut = createSutForPath(path)

        val result = sut(path)

        assertIs<SocketTimeoutException>((result as Result.Failure).error)
    }

    @Test
    fun `GIVEN local copy missing AND api call fails WHEN find repository THEN fail`() = runTest {
        coEvery { dao.get(path) } returns null
        coEvery { api.getRepository(any(), any()) } returns responseFailure
        val sut = createSutForPath(path)

        val result = sut(path)

        assertEquals("404", (result as Result.Failure).error.message)
    }

    @Test
    fun `GIVEN local copy missing AND api call succeeds WHEN find repository THEN return network copy AND cache it`() =
        runTest {
            coEvery { dao.get(path) } returns null
            coEvery { api.getRepository(any(), any()) } returns responseSuccess
            val sut = createSutForPath(path)

            val result = sut(path)

            assertEquals(repository, (result as Result.Success).value)

            coVerify { dao.insertAll(repository) }
        }
}
