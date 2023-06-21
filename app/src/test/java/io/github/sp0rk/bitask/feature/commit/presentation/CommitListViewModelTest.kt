package io.github.sp0rk.bitask.feature.commit.presentation

import app.cash.turbine.test
import io.github.sp0rk.bitask.feature.commit.domain.CommitPagingSourceFactory
import io.github.sp0rk.bitask.feature.commit.domain.CommitRemoteMediatorFactory
import io.github.sp0rk.bitask.test.MainDispatcherRule
import io.github.sp0rk.data.db.entity.Commit
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class CommitListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val commitRemoteMediatorFactory: CommitRemoteMediatorFactory = mockk()
    private val commitPagingSourceFactory: CommitPagingSourceFactory = mockk()
    private val sut = CommitListViewModel(commitRemoteMediatorFactory, commitPagingSourceFactory)
    private val commit1 = Commit("sha1", "author1", "2023‐06‐20T15:31:23Z", "msg1", 0L)
    private val commit2 = Commit("sha2", "author2", "2023‐06‐20T15:31:23Z", "msg2", 0L)
    private val commit3 = Commit("sha3", "author3", "2023‐06‐20T15:31:23Z", "msg3", 0L)

    @Test
    fun `GIVEN some commits are added WHEN share THEN correct commits are shared`() = runTest {
        // Select 3 commits
        sut.select(commit1)
        sut.select(commit2)
        sut.select(commit3)
        // Deselect 2nd commit
        sut.select(commit2)

        sut.events.test {
            sut.share()

            val event = awaitItem() as CommitListViewModel.Event.Share
            assertEquals(
                """
                    {sha1} by author1: msg1
                    {sha3} by author3: msg3
                """.trimIndent(),
                event.message
            )
        }
    }

    @Test
    fun `GIVEN no commits are added WHEN share THEN prompt selection`() = runTest {
        // Select a commit
        sut.select(commit1)
        // Deselect the commit
        sut.select(commit1)

        sut.events.test {
            sut.share()

            assertEquals(
                CommitListViewModel.Event.PromptSelection,
                awaitItem()
            )
        }
    }
}
