package com.example.gitusersassignment.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : UiEvent, State : UiState> : ViewModel() {

    // Create initial state for View
    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State

    // Get current state
    val currentState: State get() = uiState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        subscribeToEvents()
    }

    /**
     * Set new event
     */
    fun setEvent(event: Event) = viewModelScope.launch {
        _uiEvent.emit(event)
    }

    /**
     * Set new UI state
     */
    protected fun setUiState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    /**
     * Start listening to events
     */
    private fun subscribeToEvents() {
        viewModelScope.launch {
            uiEvent.collect { event -> handleIntent(event) }
        }
    }

    /**
     * Handle each intent
     */
    abstract fun handleIntent(event: Event)

}
