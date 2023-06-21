package io.github.sp0rk.bitask.feature.repository.presentation

import app.cash.turbine.test
import io.github.sp0rk.bitask.feature.repository.domain.GetRepositoriesUseCase
import io.github.sp0rk.bitask.feature.repository.domain.getRepositories
import io.github.sp0rk.bitask.feature.search.presentation.SearchViewModel
import io.github.sp0rk.data.db.dao.RepositoryDao
import io.github.sp0rk.data.db.entity.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RepositoryListViewModelTest {
    private val getRepositoriesUseCase = mockk<GetRepositoriesUseCase>()
    private val path = "sp0rk/spork"
    private val repository = Repository(0L, path)
    private val repositories = flow { emit(listOf(repository)) }
    private fun createSut() = RepositoryListViewModel(getRepositoriesUseCase)

    @Test
    fun `WHEN init THEN call useCase`() = runTest {
        every { getRepositoriesUseCase.invoke() } returns repositories

        createSut().repositories.test {
            assertEquals(listOf(repository), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
