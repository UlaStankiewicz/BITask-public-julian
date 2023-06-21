package io.github.sp0rk.bitask.feature.search.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sp0rk.bitask.R
import io.github.sp0rk.bitask.feature.commit.domain.model.Result
import io.github.sp0rk.bitask.feature.search.domain.FindRepositoryUseCase
import io.github.sp0rk.data.db.entity.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val findRepository: FindRepositoryUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Idle)
    val state: Flow<State> = _state
    private val _events = MutableSharedFlow<Event>()
    val events: Flow<Event> = _events

    fun findRepository(input: String) = viewModelScope.launch {
        if (Regex(".+/.+").matches(input)) {
            _state.emit(State.Loading)

            when (val result = findRepository.invoke(input)) {
                is Result.Success -> {
                    _state.emit(State.Idle)
                    _events.emit(Event.Success(result.value))
                }

                is Result.Failure -> {
                    _state.emit(State.Error(errorMessage = result.error.message))
                }
            }

        } else {
            _state.emit(State.Error(errorMessageRes = R.string.error_incorrect_input))
        }
    }

    sealed class State {
        object Idle : State()
        data class Error(
            @StringRes val errorMessageRes: Int? = null,
            val errorMessage: String? = null
        ) : State()

        object Loading : State()
    }

    sealed class Event {
        data class Success(val repository: Repository) : Event()
    }
}
