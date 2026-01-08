@file:Suppress("unused")

package ir.farsroidx.andromeda.viewmodel

import ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProvider
import ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProviderImpl
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ir.farsroidx.andromeda.viewmodel.contract.AndromedaViewEvent as ViewEvent
import ir.farsroidx.andromeda.viewmodel.contract.AndromedaViewState as ViewState

/**
 * Base ViewModel for MVVM architecture with reactive state and one-time events.
 *
 * Provides:
 * - [StateFlow] for UI state
 * - [SharedFlow] for one-time events
 * - Coroutine-friendly async launchers with exception handling
 * - Thread-safe updateState computation for heavy operations
 *
 * Generic parameters:
 * @param S The type of UI state. Can be a data class, primitive type, or Any.
 * @param E The type of one-time events. Can be navigation actions, Toasts, dialogs, etc.
 *
 * ## Example Usage
 *
 * ```kotlin
 *
 * data class HomeState(
 *     val items: List<String> = emptyList()
 * ) : AndromedaViewState
 *
 * sealed class HomeEvent : AndromedaViewEvent {
 *
 *     data class ShowToast(val error: String) : HomeEvent()
 *
 * }
 *
 * class HomeViewModel(
 *     private val repository: HomeRepository
 * ) : BaseMvvmViewModel<HomeState, HomeEvent>() {
 *
 *     override fun getInitialState(): HomeState = HomeState()
 *
 *     fun loadItems() {
 *
 *         ioLaunch {
 *
 *             try {
 *
 *                 val items = repository.getItems()
 *
 *                 updateState { copy(items = items) }  // thread-safe
 *
 *             } catch (t: Throwable) {
 *
 *                 sendEvent { HomeEvent.ShowToast(t.message) }
 *
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * Notes:
 * - State updates are computed in [ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProvider.default] and applied on main thread.
 * - Events are emitted safely from any thread using [ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProvider.io].
 * - Subclasses define initial state and manage their own state transformations.
 *
 * @property viewState The observable [StateFlow] representing the current UI state.
 * @property viewEvent The observable [SharedFlow] representing one-time events.
 */
abstract class AndromedaMvvmViewModel<S : ViewState, E : ViewEvent>(
    dispatcherProvider: AndromedaDispatcherProvider = AndromedaDispatcherProviderImpl,
) : AndromedaViewModel(dispatcherProvider) {
    private val defaultState: S by lazy { getInitialState() }

    private val _viewState = MutableStateFlow(value = defaultState)
    val viewState: StateFlow<S> = _viewState.asStateFlow()

    private val _viewEvent = MutableSharedFlow<E>()
    val viewEvent: SharedFlow<E> = _viewEvent.asSharedFlow()

    /**
     * Returns the initial value of the UI state.
     * Must be implemented by subclasses.
     */
    protected abstract fun getInitialState(): S

    /**
     * Updates the current UI state using an immutable-like transformation.
     *
     * The new state is computed on [AndromedaDispatcherProvider.default]
     * and applied on the main thread to keep UI updates safe.
     *
     * Example:
     * ```
     * updateState { copy(isLoading = true) }
     * ```
     *
     * @param block A transformation function that takes the current state
     * and returns the new state.
     */
    protected suspend fun updateState(block: S.() -> S) {
        val newState = defaultContext { _viewState.value.block() }

        mainContext { _viewState.value = newState }
    }

    /**
     * Emits a one-time UI event.
     *
     * Safe to call from any thread.
     * Events are emitted on [AndromedaDispatcherProvider.io].
     *
     * Example:
     * ```
     * sendEvent { HomeEvent.ShowToast }
     * ```
     *
     * @param block A lambda returning the event to emit.
     */
    protected fun sendEvent(block: () -> E) {
        ioLaunch { _viewEvent.emit(block.invoke()) }
    }

    /**
     * Resets the state to the initial state provided by [getInitialState].
     * This is useful for resetting the UI to its default state.
     *
     * Example:
     * ```
     * resetState() // Reverts to the initial state
     * ```
     */
    protected suspend fun resetState() {
        updateState { defaultState }
    }
}
