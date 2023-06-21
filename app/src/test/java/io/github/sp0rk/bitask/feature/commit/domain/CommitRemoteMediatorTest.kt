package io.github.sp0rk.bitask.feature.commit.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.CommitRemoteKeys
import io.github.sp0rk.data.db.entity.Repository
import io.github.sp0rk.data.network.dto.AuthorDto
import io.github.sp0rk.data.network.dto.CommitDto
import io.github.sp0rk.data.network.dto.CommitInfoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalPagingApi::class)
class CommitRemoteMediatorTest {
    private val repository: Repository = Repository(0L, "sp0rk/spork")
    private val pageSize = 10
    private val remoteKeys = CommitRemoteKeys("sha", 0L, 2, 4)
    private val commits =
        listOf(CommitDto("sha", CommitInfoDto(AuthorDto("author", "2023‐06‐20T15:31:23Z"), "msg")))
    private val response = Response.success(commits)

    private val getCommitsUseCase = mockk<GetCommitsUseCase>()
    private val upsertCommitsForPageUseCase: UpsertCommitsForPageUseCase = mockk()
    private val getClosestRemoteKeyUseCase: GetClosestRemoteKeyUseCase = mockk()
    private val getRemoteKeyForLastItemUseCase: GetRemoteKeyForLastItemUseCase = mockk()
    private val state = mockk<PagingState<Int, Commit>> {
        every { config } returns PagingConfig(pageSize)
    }

    private val sut = CommitRemoteMediator(
        getCommitsUseCase,
        upsertCommitsForPageUseCase,
        getClosestRemoteKeyUseCase,
        getRemoteKeyForLastItemUseCase,
        repository
    )

    @Test
    fun `GIVEN no page needed WHEN load THEN return fast`() = runTest {
        val result = sut.load(LoadType.PREPEND, state)

        assertEquals(true, (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { getCommitsUseCase(any(), any(), any()) }
    }

    @Test
    fun `GIVEN refresh WHEN load THEN load correct page`() = runTest {
        coEvery { getClosestRemoteKeyUseCase(state) } returns remoteKeys
        coEvery { getCommitsUseCase(any(), any(), any()) } returns response
        coEvery { upsertCommitsForPageUseCase(any(), any(), any(), any(), any()) } returns true

        sut.load(LoadType.REFRESH, state)

        coVerify { getCommitsUseCase(repository.path, 3, pageSize) }
    }

    @Test
    fun `GIVEN refresh AND no closest remote key WHEN load THEN load page #0`() = runTest {
        coEvery { getClosestRemoteKeyUseCase(state) } returns null
        coEvery { getCommitsUseCase(any(), any(), any()) } returns response
        coEvery { upsertCommitsForPageUseCase(any(), any(), any(), any(), any()) } returns true

        sut.load(LoadType.REFRESH, state)

        coVerify { getCommitsUseCase.invoke(repository.path, 0, pageSize) }
    }

    @Test
    fun `GIVEN getCommits throws WHEN load THEN return error`() = runTest {
        coEvery { getClosestRemoteKeyUseCase(state) } returns null
        coEvery { getCommitsUseCase(any(), any(), any()) } throws IOException()

        val result = sut.load(LoadType.REFRESH, state)

        assertIs<RemoteMediator.MediatorResult.Error>(result)
    }

    @Test
    fun `GIVEN getCommits returns null WHEN load THEN return error`() = runTest {
        coEvery { getClosestRemoteKeyUseCase(state) } returns null
        coEvery { getCommitsUseCase(any(), any(), any()) } returns Response.success(null)

        val result = sut.load(LoadType.REFRESH, state)

        assertIs<RemoteMediator.MediatorResult.Error>(result)
    }

    @Test
    fun `GIVEN getCommits returns commits WHEN load THEN return success`() = runTest {
        coEvery { getClosestRemoteKeyUseCase(state) } returns null
        coEvery { getCommitsUseCase(any(), any(), any()) } returns response
        coEvery { upsertCommitsForPageUseCase(any(), any(), any(), any(), any()) } returns true

        val result = sut.load(LoadType.REFRESH, state)

        assertEquals(true, (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }
}
