package io.github.sp0rk.bitask.feature.search.presentation

import app.cash.turbine.test
import app.cash.turbine.testIn
import io.github.sp0rk.bitask.R
import io.github.sp0rk.bitask.feature.commit.domain.model.Result
import io.github.sp0rk.bitask.feature.search.domain.FindRepositoryUseCase
import io.github.sp0rk.bitask.test.MainDispatcherRule
import io.github.sp0rk.data.db.entity.Repository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.net.SocketTimeoutException
import kotlin.test.assertEquals


class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val findRepositoryUseCase = mockk<FindRepositoryUseCase>()
    private val sut = SearchViewModel(findRepositoryUseCase)
    private val inputInvalid = "spork"
    private val inputValid = "sp0rk/spork"
    private val repository = Repository(0L, inputValid)

    @Test
    fun `GIVEN input invalid WHEN find repository THEN emit error`() = runTest {
        sut.findRepository(inputInvalid)

        sut.state.test {
            assertEquals(SearchViewModel.State.Error(R.string.error_incorrect_input), awaitItem())
        }
    }

    @Test
    fun `GIVEN success WHEN find repository THEN emit success AND idle state`() = runTest {
        coEvery { findRepositoryUseCase.invoke(inputValid) } returns Result.Success(
            repository
        )
        val states = sut.state.testIn(backgroundScope)
        val events = sut.events.testIn(backgroundScope)

        sut.findRepository(inputValid)

        assertEquals(SearchViewModel.State.Idle, states.awaitItem())
        assertEquals(SearchViewModel.Event.Success(repository), events.awaitItem())
    }

    @Test
    fun `GIVEN failure WHEN find repository THEN emit error`() = runTest {
        coEvery { findRepositoryUseCase.invoke(inputValid) } returns Result.Failure(
            SocketTimeoutException("Timeout")
        )
        sut.findRepository(inputValid)

        sut.state.test {
            assertEquals(SearchViewModel.State.Error(errorMessage = "Timeout"), awaitItem())
        }
    }
}
