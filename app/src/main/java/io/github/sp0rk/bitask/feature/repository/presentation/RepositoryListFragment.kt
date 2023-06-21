package io.github.sp0rk.bitask.feature.repository.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.sp0rk.bitask.R
import io.github.sp0rk.bitask.databinding.FragmentRepositoryListBinding
import io.github.sp0rk.data.db.entity.Repository
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepositoryListFragment : Fragment(R.layout.fragment_repository_list) {
    private val binding: FragmentRepositoryListBinding by viewBinding()
    private val viewModel: RepositoryListViewModel by viewModel()
    private val adapter get() = binding.repositories.adapter as RepositoryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupFab()
        collectRepositoryData()
    }

    private fun setupList() {
        binding.repositories.adapter = RepositoryListAdapter(
            onItemClicked = ::navigateToCommits,
        )
    }

    private fun setupFab() {
        binding.findRepository.setOnClickListener {
            navigateToSearch()
        }
    }

    private fun collectRepositoryData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repositories.collect {
                    render(it)
                }
            }
        }
    }

    private fun render(state: List<Repository>) {
        adapter.submitList(state)
    }

    private fun navigateToCommits(repository: Repository) {
        findNavController().navigate(
            RepositoryListFragmentDirections.repositoriesToCommits(
                repository
            )
        )
    }

    private fun navigateToSearch() {
        findNavController().navigate(RepositoryListFragmentDirections.repositoriesToSearch())
    }
}

