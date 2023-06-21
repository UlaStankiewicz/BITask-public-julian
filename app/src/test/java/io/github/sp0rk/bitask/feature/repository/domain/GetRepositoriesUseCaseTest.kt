package io.github.sp0rk.bitask.feature.repository.domain

import app.cash.turbine.test
import io.github.sp0rk.data.db.dao.RepositoryDao
import io.github.sp0rk.data.db.entity.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetRepositoriesUseCaseTest {
    private val dao = mockk<RepositoryDao>()
    private val path = "sp0rk/spork"
    private val repository = Repository(0L, path)
    private val repositories = flow { emit(listOf(repository)) }

    private val sut = GetRepositoriesUseCase {
        getRepositories(dao)
    }

    @Test
    fun `WHEN get repositories THEN call dao`() = runTest {
        coEvery { dao.getAll() } returns repositories

        val result = sut()

        coVerify { dao.getAll() }
        result.test {
            assertEquals(listOf(repository), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
