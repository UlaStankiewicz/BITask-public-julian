package io.github.sp0rk.bitask.feature.search.presentation

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.sp0rk.bitask.R
import io.github.sp0rk.bitask.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : DialogFragment(R.layout.fragment_search) {
    private val binding: FragmentSearchBinding by viewBinding()
    private val viewModel: SearchViewModel by viewModel()

    override fun onStart() {
        super.onStart()
        setSize(widthPercentage = 90)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.findRepository.setOnClickListener {
            viewModel.findRepository(binding.input.text.toString())
        }
        collectState()
        collectEvent()
    }

    private fun collectState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    render(it)
                }
            }
        }
    }

    private fun collectEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect {
                    handle(it)
                }
            }
        }
    }

    private fun handle(event: SearchViewModel.Event) = when (event) {
        is SearchViewModel.Event.Success -> findNavController().navigate(
            SearchFragmentDirections.searchToCommits(
                event.repository
            )
        )
    }

    private fun render(state: SearchViewModel.State) = when (state) {
        is SearchViewModel.State.Error -> {
            binding.inputLayout.error =
                state.errorMessageRes?.let(::getString) ?: state.errorMessage ?: "Error"
            binding.progress.visibility = View.INVISIBLE
        }

        SearchViewModel.State.Idle -> {
            binding.inputLayout.error = null
            binding.progress.visibility = View.INVISIBLE
        }

        SearchViewModel.State.Loading -> {
            binding.progress.visibility = View.VISIBLE
        }
    }


    @Suppress("SameParameterValue")
    private fun setSize(widthPercentage: Int) {
        val newWidth = widthPercentage.toFloat() / 100

        val rect = with(Resources.getSystem().displayMetrics) {
            Rect(0, 0, widthPixels, heightPixels)
        }

        val percentWidth = rect.width() * newWidth

        dialog?.window?.run {
            setLayout(percentWidth.toInt(), attributes.height)
        }
    }
}

