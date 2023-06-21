package io.github.sp0rk.bitask.feature.commit.domain

import androidx.paging.PagingState
import androidx.room.withTransaction
import io.github.sp0rk.bitask.test.mockTransactionsInDb
import io.github.sp0rk.data.db.LocalDatabase
import io.github.sp0rk.data.db.dao.CommitRemoteKeyDao
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.CommitRemoteKeys
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetClosestRemoteKeyUseCaseTest {
    private val sha = "sha"
    private val position = 1
    private val commit = Commit(sha, "author", "2023‐06‐20T15:31:23Z", "msg", 0L)
    private val remoteKeys = CommitRemoteKeys(sha, 0L, null, 1)

    private val dao = mockk<CommitRemoteKeyDao> {
        coEvery { remoteKeysBySha(sha) } returns remoteKeys
    }
    private val db = mockk<LocalDatabase> {
        every { commitRemoteKeysDao() } returns dao
    }
    private val state = mockk<PagingState<Int, Commit>>()

    private val sut = GetClosestRemoteKeyUseCase {
        getClosestRemoteKey(db, state)
    }

    @Before
    fun initMocks() {
        mockTransactionsInDb(this, db)
    }

    @Test
    fun `WHEN closest item has sha THEN call dao in transaction`() = runTest {
        every { state.anchorPosition } returns position
        every { state.closestItemToPosition(position) } returns commit

        val result = sut(state)

        coVerify { db.withTransaction(any()) }
        coVerify { dao.remoteKeysBySha(sha) }
        assertEquals(remoteKeys, result)
    }

    @Test
    fun `WHEN closest item missing THEN return null`() = runTest {
        every { state.anchorPosition } returns position
        every { state.closestItemToPosition(position) } returns null

        val result = sut(state)

        assertNull(result)
    }

    @Test
    fun `WHEN anchor position missing THEN return null`() = runTest {
        every { state.anchorPosition } returns null

        val result = sut(state)

        assertNull(result)
    }
}
