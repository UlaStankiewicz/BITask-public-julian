package io.github.sp0rk.bitask.feature.repository.presentation

import androidx.lifecycle.ViewModel
import io.github.sp0rk.data.db.entity.Repository
import io.github.sp0rk.bitask.feature.repository.domain.GetRepositoriesUseCase
import kotlinx.coroutines.flow.Flow

class RepositoryListViewModel(
    getRepositories: GetRepositoriesUseCase,
) : ViewModel() {
    val repositories: Flow<List<Repository>> = getRepositories()
}
