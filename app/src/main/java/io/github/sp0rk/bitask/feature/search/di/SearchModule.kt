package io.github.sp0rk.bitask.feature.search.di

import io.github.sp0rk.bitask.feature.search.domain.FindRepositoryUseCase
import io.github.sp0rk.bitask.feature.search.domain.findRepository
import io.github.sp0rk.bitask.feature.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

fun searchModule() = module {
    single { FindRepositoryUseCase { path -> findRepository(get(), get(), path) } }
    viewModelOf(::SearchViewModel)
}
