package io.github.sp0rk.bitask.feature.commit.domain

import io.github.sp0rk.data.db.entity.Repository

class CommitRemoteMediatorFactory(
    private val getCommitsUseCase: GetCommitsUseCase,
    private val upsertCommitsForPageUseCase: UpsertCommitsForPageUseCase,
    private val getClosestRemoteKeyUseCase: GetClosestRemoteKeyUseCase,
    private val getRemoteKeyForLastItemUseCase: GetRemoteKeyForLastItemUseCase,
) {
    operator fun invoke(repository: Repository) = CommitRemoteMediator(
        getCommitsUseCase,
        upsertCommitsForPageUseCase,
        getClosestRemoteKeyUseCase,
        getRemoteKeyForLastItemUseCase,
        repository
    )
}
