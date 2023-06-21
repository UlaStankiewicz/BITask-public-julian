package io.github.sp0rk.bitask.feature.commit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import io.github.sp0rk.bitask.feature.commit.domain.CommitPagingSourceFactory
import io.github.sp0rk.bitask.feature.commit.domain.CommitRemoteMediatorFactory
import io.github.sp0rk.bitask.feature.commit.presentation.model.CommitListItem
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CommitListViewModel(
    private val commitRemoteMediatorFactory: CommitRemoteMediatorFactory,
    private val commitPagingSourceFactory: CommitPagingSourceFactory
) : ViewModel() {

    private val selectedCommits: MutableSet<Commit> = mutableSetOf()
    private val _events = MutableSharedFlow<Event>()
    val events: Flow<Event> = _events

    fun select(commit: Commit): Boolean {
        if (commit in selectedCommits) {
            selectedCommits.remove(commit)
        } else {
            selectedCommits.add(commit)
        }
        return commit in selectedCommits
    }

    fun share() = viewModelScope.launch {
        _events.emit(
            if (selectedCommits.isEmpty()) {
                Event.PromptSelection
            } else {
                Event.Share(
                    selectedCommits.joinToString(
                        separator = "\n",
                        transform = Commit::getShareableString
                    )
                )
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    fun queryCommits(
        repository: Repository,
    ): Flow<PagingData<CommitListItem>> = Pager(
        config = PagingConfig(pageSize = 100),
        remoteMediator = commitRemoteMediatorFactory(repository)
    ) {
        commitPagingSourceFactory(repository.id)
    }.flow.map { pagingData ->
        pagingData.map { commit ->
            CommitListItem(commit, selected = commit in selectedCommits)
        }
    }.cachedIn(viewModelScope)

    sealed class Event {
        data class Share(val message: String) : Event()
        object PromptSelection : Event()
    }
}

private fun Commit.getShareableString() = "{$sha} by $author: $message"
