package io.github.sp0rk.bitask.feature.repository.di

import io.github.sp0rk.data.db.LocalDatabase
import io.github.sp0rk.bitask.feature.repository.domain.GetRepositoriesUseCase
import io.github.sp0rk.bitask.feature.repository.domain.getRepositories
import io.github.sp0rk.bitask.feature.repository.presentation.RepositoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

fun repositoryModule() = module {
    single { get<LocalDatabase>().repositoryDao() }
    single { GetRepositoriesUseCase { getRepositories(get()) } }
    viewModelOf(::RepositoryListViewModel)
}
