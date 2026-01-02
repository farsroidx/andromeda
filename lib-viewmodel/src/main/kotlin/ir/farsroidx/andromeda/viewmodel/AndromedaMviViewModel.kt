@file:Suppress("unused")

package ir.farsroidx.andromeda.viewmodel

import ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProvider
import ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProviderImpl
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import ir.farsroidx.andromeda.viewmodel.contract.AndromedaUiAction as UiAction
import ir.farsroidx.andromeda.viewmodel.contract.AndromedaUiEffect as UiEffect
import ir.farsroidx.andromeda.viewmodel.contract.AndromedaUiIntent as UiIntent
import ir.farsroidx.andromeda.viewmodel.contract.AndromedaUiState as UiState

/**
 * Base ViewModel implementation for **MVI (Model–View–Intent)** architecture.
 *
 * This ViewModel is designed to be **flexible, scalable, and pragmatic** rather than dogmatic.
 * It supports both simple and complex MVI flows while keeping boilerplate minimal.
 *
 * ---
 *
 * ## Architecture Overview
 *
 * ```
 * UI ── Intent ──▶ ViewModel ── Action ──▶ State
 *                      └─────── Effect ──▶ UI (one-time)
 * ```
 *
 * - **Intent ([UiIntent])**: User intention or UI trigger (clicks, lifecycle, retries)
 * - **Action ([UiAction])**: Internal representation of work or results
 * - **State ([UiState])**: Immutable UI state, single source of truth
 * - **Effect ([UiEffect])**: One-time side effects (navigation, toast, etc...)
 *
 * ---
 *
 * ## Key Characteristics
 *
 * - Thread-safe state updates
 * - Clear separation of Intent vs Action
 * - One-way data flow
 * - Coroutine-first design
 * - No forced reducer-style boilerplate
 *
 * ---
 *
 * @param S UI State type. Must implement [UiState].
 * @param I UI Intent type. Must implement [UiIntent].
 * @param A Internal Action type. Must implement [UiAction].
 * @param E One-time UI Effect type. Must implement [UiEffect].
 *
 * @param dispatcherProvider Provides coroutine dispatchers for threading control.
 */
abstract class AndromedaMviViewModel <S: UiState, I: UiIntent, A: UiAction, E: UiEffect> (
    dispatcherProvider: AndromedaDispatcherProvider = AndromedaDispatcherProviderImpl
) : AndromedaViewModel(dispatcherProvider) {

    // ---------------------------------------------------------------------------------------------
    // State
    // ---------------------------------------------------------------------------------------------

    /**
     * Lazily initialized default UI state.
     * Used as the initial value and for [resetState].
     */
    private val defaultState: S by lazy { getInitialState() }

    private val _uiState = MutableStateFlow(value = defaultState)

    /**
     * Public immutable stream of UI state.
     *
     * The UI must render **only** based on this state.
     */
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    /**
     * Provides the initial UI state.
     *
     * Example:
     * ```
     * override fun getInitialState(): HomeState =
     *     HomeState(isLoading = false, items = emptyList())
     * ```
     */
    protected abstract fun getInitialState(): S

    /**
     * Updates the current UI state using an immutable transformation.
     *
     * - Computation runs on [AndromedaDispatcherProvider.default]
     * - State assignment happens on Main thread
     *
     * Example:
     * ```
     * updateState { copy(isLoading = true) }
     * ```
     */
    protected suspend fun updateState(block: S.() -> S) {

        val newState = defaultContext { _uiState.value.block() }

        mainContext { _uiState.value = newState }
    }

    /**
     * Resets the UI state back to its initial value.
     *
     * Useful for logout flows, retry scenarios, or screen resets.
     *
     * Example:
     * ```
     * resetState()
     * ```
     */
    protected suspend fun resetState() {
        updateState { defaultState }
    }

    // ---------------------------------------------------------------------------------------------
    // Intent
    // ---------------------------------------------------------------------------------------------

    /**
     * Stream of UI intents.
     *
     * Intents represent **what the user wants**, not what the system does.
     */
    private val _intents: MutableSharedFlow<I> = MutableSharedFlow(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init { observeIntents() }

    /**
     * Observes incoming intents and delegates them to [onIntentReceived].
     */
    private fun observeIntents() {
        defaultLaunch {
            _intents.collect(collector = ::onIntentReceived)
        }
    }

    /**
     * Sends a new intent to the ViewModel.
     *
     * Intended to be called directly from UI.
     *
     * Example:
     * ```
     * viewModel.newIntent { HomeIntent.Load }
     * ```
     */
    fun newIntent(block: () -> I) {
        _intents.tryEmit(value = block.invoke())
    }

    /**
     * Handles an incoming intent.
     *
     * This is typically where intents are **mapped to actions**.
     *
     * Example:
     * ```
     * override fun onIntentReceived(intent: HomeIntent) {
     *     when (intent) {
     *         HomeIntent.Load -> dispatch { HomeAction.Load }
     *     }
     * }
     * ```
     */
    protected open fun onIntentReceived(intent: I) {
        // Default implementation does nothing
    }

    // ---------------------------------------------------------------------------------------------
    // Action
    // ---------------------------------------------------------------------------------------------

    /**
     * Channel for internal actions.
     *
     * Actions represent **what actually happens** inside the ViewModel.
     */
    private val _actions = Channel<A>(capacity = Channel.BUFFERED)

    init { observeActions() }

    /**
     * Observes actions and forwards them to [processActions].
     */
    private fun observeActions() {
        defaultLaunch {
            _actions.consumeAsFlow().collect(collector = ::processActions)
        }
    }

    /**
     * Dispatches an internal action.
     *
     * Example:
     * ```
     * dispatch { HomeAction.Loading }
     * dispatch { HomeAction.Loaded(items) }
     * ```
     */
    protected fun dispatch(block: () -> A) {
        _actions.trySend(element = block.invoke())
    }

    /**
     * Processes an action and updates state or triggers effects.
     *
     * This is the **core business logic entry point**.
     *
     * Example:
     * ```
     * override fun processActions(action: HomeAction) {
     *     when (action) {
     *         is HomeAction.Loading -> loadItems()
     *         is HomeAction.Loaded  -> updateState {
     *             copy(items = action.items, isLoading = false)
     *         }
     *     }
     * }
     * ```
     */
    protected open fun processActions(action: A) {
        // Default implementation does nothing
    }

    // ---------------------------------------------------------------------------------------------
    // Effect
    // ---------------------------------------------------------------------------------------------

    /**
     * Stream of one-time UI effects.
     *
     * Effects must **never** be part of the UI state.
     */
    private val _uiEffects: MutableSharedFlow<E> = MutableSharedFlow(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Public immutable stream of UI effects.
     */
    val uiEffects: Flow<E> = _uiEffects.asSharedFlow()

    /**
     * Emits a one-time UI effect.
     *
     * Example:
     * ```
     * sendEffect { HomeEffect.ShowError }
     * sendEffect { HomeEffect.NavigateToDetails(id) }
     * ```
     */
    protected fun sendEffect(block: () -> E) {
        ioLaunch { _uiEffects.emit(value = block.invoke()) }
    }
}