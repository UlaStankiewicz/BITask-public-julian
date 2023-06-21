package io.github.sp0rk.bitask.feature.commit.domain

import androidx.room.withTransaction
import io.github.sp0rk.bitask.test.mockTransactionsInDb
import io.github.sp0rk.data.db.LocalDatabase
import io.github.sp0rk.data.db.dao.CommitDao
import io.github.sp0rk.data.db.dao.CommitRemoteKeyDao
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.CommitRemoteKeys
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.any
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UpsertCommitsForPageUseCaseTest {
    private val repositoryId = 1L
    private val page = 0
    private val pageSize = 3
    private val commits = listOf(Commit("sha", "author", "2023‐06‐20T15:31:23Z", "msg", 0L))
    private val commits5 = buildList {
        repeat(5) {
            addAll(commits)
        }
    }

    private val commitDao = mockk<CommitDao>(relaxed = true)
    private val remoteKeysDao = mockk<CommitRemoteKeyDao>(relaxed = true)
    private val db = mockk<LocalDatabase> {
        every { commitRemoteKeysDao() } returns remoteKeysDao
        every { commitDao() } returns commitDao
    }

    private val sut = UpsertCommitsForPageUseCase { commits, _, _, forceRefresh, _ ->
        upsertCommitsForPage(db, commits, pageSize, page, forceRefresh, repositoryId)
    }

    @Before
    fun initMocks() {
        mockTransactionsInDb(this, db)
    }

    @Test
    fun `WHEN upsert THEN insert commits and remoteKeys`() = runTest {
        sut(commits, page, pageSize, false, repositoryId)

        coVerify { db.withTransaction(any()) }
        coVerify { commitDao.insertAll(commits) }
        coVerify { remoteKeysDao.insertAll(listOf(CommitRemoteKeys("sha", 1L, null, null))) }
    }

    @Test
    fun `GIVEN forceRefresh WHEN upsert THEN clear commits AND remote keys`() = runTest {
        sut(commits, page, pageSize, true, repositoryId)

        coVerify { db.withTransaction(any()) }
        coVerify { commitDao.clearFromRepository(repositoryId) }
        coVerify { remoteKeysDao.clearRemoteKeys(repositoryId) }
    }

    @Test
    fun `GIVEN no forceRefresh WHEN upsert THEN don't clear commits NOR remote keys`() = runTest {
        sut(commits, page, pageSize, false, repositoryId)

        coVerify { db.withTransaction(any()) }
        coVerify(exactly = 0) { commitDao.clearFromRepository(repositoryId) }
        coVerify(exactly = 0) { remoteKeysDao.clearRemoteKeys(repositoryId) }
    }

    @Test
    fun `GIVEN count less than pageSize WHEN upsert THEN return true`() = runTest {
        val result = sut(commits, page, pageSize, false, repositoryId)

        assertEquals(true, result)
    }

    @Test
    fun `GIVEN pageSize less than count WHEN upsert THEN return false`() = runTest {
        val result = sut(commits5, page, pageSize, false, repositoryId)

        assertEquals(false, result)
    }
}
