package io.github.sp0rk.bitask.feature.commit.di

import io.github.sp0rk.bitask.feature.commit.domain.CommitPagingSourceFactory
import io.github.sp0rk.bitask.feature.commit.domain.CommitRemoteMediatorFactory
import io.github.sp0rk.bitask.feature.commit.domain.GetClosestRemoteKeyUseCase
import io.github.sp0rk.bitask.feature.commit.domain.GetCommitsUseCase
import io.github.sp0rk.bitask.feature.commit.domain.GetRemoteKeyForLastItemUseCase
import io.github.sp0rk.bitask.feature.commit.domain.UpsertCommitsForPageUseCase
import io.github.sp0rk.bitask.feature.commit.domain.getClosestRemoteKey
import io.github.sp0rk.bitask.feature.commit.domain.getCommits
import io.github.sp0rk.bitask.feature.commit.domain.getRemoteKeyForLastItem
import io.github.sp0rk.bitask.feature.commit.domain.upsertCommitsForPage
import io.github.sp0rk.bitask.feature.commit.presentation.CommitListViewModel
import io.github.sp0rk.data.db.LocalDatabase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commitModule() = module {
    single { get<LocalDatabase>().commitDao() }
    single { get<LocalDatabase>().commitRemoteKeysDao() }

    single { GetClosestRemoteKeyUseCase { state -> getClosestRemoteKey(get(), state) } }
    single { GetRemoteKeyForLastItemUseCase { state -> getRemoteKeyForLastItem(get(), state) } }
    single {
        UpsertCommitsForPageUseCase { commits, pageSize, page, forceRefresh, repositoryId ->
            upsertCommitsForPage(get(), commits, pageSize, page, forceRefresh, repositoryId)
        }
    }
    single {
        GetCommitsUseCase { repositoryPath, page, pageSize ->
            getCommits(get(), repositoryPath, page, pageSize)
        }
    }
    singleOf(::CommitPagingSourceFactory)
    singleOf(::CommitRemoteMediatorFactory)
    viewModelOf(::CommitListViewModel)
}
