@file:Suppress("unused", "SameParameterValue")

package ir.farsroidx.andromeda.viewmodel

import android.os.Looper
import android.util.Log
import androidx.annotation.AnyThread
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProvider
import ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProviderImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A base [ViewModel] that provides coroutine-friendly utilities with safe exception handling
 * and configurable dispatchers. Designed to simplify launching coroutines and switching
 * contexts in ViewModels while maintaining a clean architecture.
 *
 * ## Features
 * - Centralized [CoroutineExceptionHandler] for logging uncaught exceptions.
 * - Convenience methods for launching coroutines on
 *      [kotlinx.coroutines.Dispatchers.IO], [kotlinx.coroutines.Dispatchers.Main], and [kotlinx.coroutines.Dispatchers.Default].
 * - Suspend context helpers ([ioContext], [mainContext], [defaultContext]) for structured concurrency.
 * - Optional injection of a [ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProvider] for testability and custom dispatchers.
 *
 * This class is architecture-agnostic and can be used in MVVM, MVI, or Clean Architecture setups.
 *
 * ### Example Usage
 *
 * ```kotlin
 * class HomeViewModel(private val repository: HomeRepository) : BaseViewModel() {
 *
 *     val data: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())
 *
 *     fun loadData() {
 *
 *         ioLaunch {
 *
 *             val items = repository.fetchItems()
 *
 *             mainContext {
 *                 data.value = items
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * ### Notes
 * - Override [onUnhandledException] to customize global exception handling (e.g., send to analytics).
 * - Use [ioLaunch], [mainLaunch], [defaultLaunch] for coroutine launching instead of `viewModelScope.launch` directly
 *   to automatically include exception handling and proper dispatchers.
 * - Use [ioContext], [mainContext], [defaultContext] for inline context switching within suspend functions.
 *
 * @property dispatcherProvider The provider for coroutine dispatchers. Defaults to [ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProviderImpl].
 */
abstract class AndromedaViewModel(
    val dispatcherProvider: AndromedaDispatcherProvider = AndromedaDispatcherProviderImpl
) : ViewModel() {


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onUnhandledException(throwable)
    }

    /**
     * Handles uncaught exceptions thrown in any coroutine launched with this ViewModel.
     *
     * Override this method to provide custom exception handling logic such as
     * sending logs to a crash reporting service.
     *
     * @param throwable The uncaught [Throwable] from a coroutine.
     */
    @CallSuper
    protected open fun onUnhandledException(throwable: Throwable) {

        // Default behavior: Log the exception to a crash reporting service or analytics

        // Example: FirebaseCrashlytics.getInstance().recordException(throwable)

        Log.e("AndromedaViewModel", throwable.message, throwable)
    }

    /**
     * Launches a coroutine on the given [dispatcher] with the internal [CoroutineExceptionHandler].
     *
     * @param dispatcher The [CoroutineDispatcher] to run the coroutine on. Defaults to [AndromedaDispatcherProvider.main].
     * @param start The [CoroutineStart] strategy.
     * @param block The suspend lambda to execute in the coroutine.
     */
    private fun dispatcherLaunch(
        dispatcher: CoroutineDispatcher = dispatcherProvider.main,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(
        context = dispatcher + coroutineExceptionHandler,
        start = start,
        block = block
    )

    /** Launches a coroutine on [AndromedaDispatcherProvider.io]. */
    @AnyThread
    protected fun ioLaunch(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = dispatcherLaunch(dispatcherProvider.io, start = start, block = block)

    /** Launches a coroutine on [AndromedaDispatcherProvider.main]. */
    @MainThread
    protected fun mainLaunch(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = dispatcherLaunch(dispatcherProvider.main, start = start, block = block)

    /** Launches a coroutine on [AndromedaDispatcherProvider.default]. */
    @AnyThread
    protected fun defaultLaunch(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = dispatcherLaunch(dispatcherProvider.default, start = start, block = block)

    /** Executes [block] in [AndromedaDispatcherProvider.io] context. */
    protected suspend inline fun <R> ioContext(
        noinline block: suspend CoroutineScope.() -> R
    ): R = withContext(dispatcherProvider.io, block = block)

    /** Executes [block] in [AndromedaDispatcherProvider.main] context. */
    protected suspend inline fun <R> mainContext(
        noinline block: suspend CoroutineScope.() -> R
    ): R = withContext(dispatcherProvider.main, block = block)

    /** Executes [block] in [AndromedaDispatcherProvider.default] context. */
    protected suspend inline fun <R> defaultContext(
        noinline block: suspend CoroutineScope.() -> R
    ): R = withContext(dispatcherProvider.default, block = block)

    /**
     * Called when the [ViewModel] is about to be destroyed.
     * Subclasses can override to perform custom cleanup.
     */
    @CallSuper
    override fun onCleared() {
        super.onCleared()
        // viewModelScope is automatically canceled, but subclasses can add custom cleanup here
    }

    /**
     * Ensures that the current execution is on the main thread.
     *
     * Throws [IllegalStateException] if called from a background thread.
     *
     * Intended for defensive programming in ViewModels and UI layers.
     */
    fun requireMainThread() {
        check(Looper.getMainLooper().isCurrentThread) {
            "This operation must be executed on the main thread."
        }
    }

    /**
     * Ensures that the current execution is NOT on the main thread.
     *
     * Throws [IllegalStateException] if called from the main thread.
     *
     * Intended for I/O or heavy operations.
     */
    fun requireBackgroundThread() {
        check(!Looper.getMainLooper().isCurrentThread) {
            "This operation must not be executed on the main thread."
        }
    }

    /**
     * Called when the associated Composable leaves the composition or the ViewModel is no longer observed.
     *
     * This method provides a single point to perform cleanup operations such as:
     * - Canceling timers, jobs, or coroutines
     * - Removing listeners or observers
     * - Resetting temporary state
     * - Freeing resources that are no longer needed
     *
     * This is automatically invoked when used with [androidx.compose.runtime.DisposableEffect] in a Composable:
     * ```kotlin
     * @Composable
     * fun ObserveViewModelCleanup(viewModel: AndromedaViewModel) {
     *     DisposableEffect(viewModel) {
     *         onDispose { viewModel.onDispose() }
     *     }
     * }
     * ```
     *
     * Subclasses can override this method to provide custom disposal logic.
     * Make sure that any long-running or asynchronous work is safely canceled to prevent memory leaks or crashes.
     */
    open fun onDispose() {
        // cancel timers, reset state, remove listeners, etc.
    }
}