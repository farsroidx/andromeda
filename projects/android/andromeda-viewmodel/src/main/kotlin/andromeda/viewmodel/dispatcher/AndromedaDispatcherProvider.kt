package andromeda.viewmodel.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides coroutine dispatchers used across the Andromeda architecture.
 *
 * This interface acts as an abstraction layer over [kotlinx.coroutines.Dispatchers]
 * to improve testability, flexibility, and architectural clarity.
 *
 * By depending on [AndromedaDispatcherProvider] instead of directly using
 * [kotlinx.coroutines.Dispatchers], ViewModels and other components can:
 *
 * - Be easily unit-tested by injecting test dispatchers
 * - Avoid hard dependencies on Android-specific dispatchers
 * - Centralize thread policy decisions in a single place
 *
 * ## Usage
 *
 * ```kotlin
 * class ExampleViewModel(
 *     private val dispatchers: AndromedaDispatcherProvider
 * ) : ViewModel() {
 *
 *     fun load() = viewModelScope.launch(dispatchers.io) {
 *         // background work
 *     }
 * }
 * ```
 *
 * ## Thread Semantics
 *
 * - [io]      For I/O-bound work such as network or disk operations
 * - [default] For CPU-intensive or computational work
 * - [main]    For UI-related work and state updates
 */
interface AndromedaDispatcherProvider {
    /**
     * Dispatcher optimized for I/O-bound operations.
     *
     * Typical use cases:
     * - Network requests
     * - Database access
     * - File system operations
     */
    val io: CoroutineDispatcher

    /**
     * Dispatcher confined to the main thread.
     *
     * Typical use cases:
     * - UI state updates
     * - Emitting UI events
     * - Interacting with main-thread-only APIs
     */
    val main: CoroutineDispatcher

    /**
     * Dispatcher optimized for CPU-intensive work.
     *
     * Typical use cases:
     * - Data transformation
     * - JSON parsing
     * - Complex calculations
     */
    val default: CoroutineDispatcher
}
