package io.github.sp0rk.bitask.di

import io.github.sp0rk.bitask.feature.commit.di.commitModule
import io.github.sp0rk.bitask.feature.repository.di.repositoryModule
import io.github.sp0rk.bitask.feature.search.di.searchModule
import io.github.sp0rk.data.db.databaseModule
import io.github.sp0rk.data.network.networkModule
import org.koin.dsl.module

fun appModule() = module {
    includes(
        databaseModule(),
        networkModule(),
        repositoryModule(),
        searchModule(),
        commitModule(),
    )
}
