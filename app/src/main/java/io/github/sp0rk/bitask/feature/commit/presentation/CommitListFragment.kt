package io.github.sp0rk.bitask.feature.commit.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import io.github.sp0rk.bitask.R
import io.github.sp0rk.bitask.databinding.FragmentCommitListBinding
import io.github.sp0rk.bitask.feature.commit.presentation.model.CommitListItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommitListFragment : Fragment(R.layout.fragment_commit_list) {
    private val navArgs by navArgs<CommitListFragmentArgs>()
    private val binding: FragmentCommitListBinding by viewBinding()
    private val viewModel: CommitListViewModel by viewModel()
    private val adapter get() = binding.commits.adapter as CommitListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupFab()
        setupId()
        collectCommitData()
        collectLoadingState()
        collectEvents()
    }

    private fun setupId() {
        binding.repoPath.text = navArgs.repository.path
        binding.repoId.text = navArgs.repository.id.toString()
    }

    private fun setupList() {
        binding.commits.adapter = CommitListAdapter(
            onItemClicked = ::select,
        )
    }

    private fun select(item: CommitListItem) = viewModel.select(item.commit)

    private fun collectLoadingState() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.progress.isVisible = loadStates.refresh is LoadState.Loading
                (loadStates.refresh as? LoadState.Error)?.let { error ->
                    Snackbar.make(
                        requireView(),
                        error.error.message ?: getString(R.string.error_general),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setupFab() {
        binding.share.setOnClickListener {
            viewModel.share()
        }
    }

    private fun collectCommitData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.queryCommits(navArgs.repository).collect {
                    render(it)
                }
            }
        }
    }

    private fun collectEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect(::handle)
            }
        }
    }

    private suspend fun render(state: PagingData<CommitListItem>) {
        adapter.submitData(state)
    }

    private fun handle(event: CommitListViewModel.Event) = when (event) {
        is CommitListViewModel.Event.Share -> {
            share(event.message)
        }

        CommitListViewModel.Event.PromptSelection -> promptSelection()
    }

    private fun promptSelection() {
        Snackbar.make(requireView(), R.string.prompt_selection, Snackbar.LENGTH_LONG).show()
    }

    private fun share(message: String) {
        startActivity(
            Intent.createChooser(
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/plain"
                },
                null
            )
        )
    }
}

