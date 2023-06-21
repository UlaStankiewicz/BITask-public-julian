package io.github.sp0rk.bitask.feature.commit.domain

import io.github.sp0rk.data.db.entity.Repository
import io.mockk.EqMatcher
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.Test

class CommitRemoteMediatorFactoryTest {
    private val getCommitsUseCase: GetCommitsUseCase = mockk()
    private val upsertCommitsForPageUseCase: UpsertCommitsForPageUseCase = mockk()
    private val getClosestRemoteKeyUseCase: GetClosestRemoteKeyUseCase = mockk()
    private val getRemoteKeyForLastItemUseCase: GetRemoteKeyForLastItemUseCase = mockk()

    private val repository = Repository(0L, "path")
    private val sut = CommitRemoteMediatorFactory(
        getCommitsUseCase,
        upsertCommitsForPageUseCase,
        getClosestRemoteKeyUseCase,
        getRemoteKeyForLastItemUseCase
    )

    @Test
    fun `WHEN invoked THEN return CommitRemoteMediator`() {
        mockkConstructor(CommitRemoteMediator::class)

        sut(repository)

        verify {
            constructedWith<CommitRemoteMediator>(
                EqMatcher(getCommitsUseCase),
                EqMatcher(upsertCommitsForPageUseCase),
                EqMatcher(getClosestRemoteKeyUseCase),
                EqMatcher(getRemoteKeyForLastItemUseCase),
                EqMatcher(repository)
            )
        }
    }
}
