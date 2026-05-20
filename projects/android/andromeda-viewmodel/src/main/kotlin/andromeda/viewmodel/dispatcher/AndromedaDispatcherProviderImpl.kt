package andromeda.viewmodel.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Default implementation of [AndromedaDispatcherProvider].
 *
 * This implementation delegates directly to standard
 * [kotlinx.coroutines.Dispatchers].
 *
 * It should be used in production code, while tests are encouraged
 * to provide their own implementations with test dispatchers
 * (e.g. `kotlinx.coroutines.test.StandardTestDispatcher`).
 */
object AndromedaDispatcherProviderImpl : AndromedaDispatcherProvider {
    /** Uses [Dispatchers.IO] for I/O-bound operations. */
    override val io: CoroutineDispatcher = Dispatchers.IO

    /** Uses [Dispatchers.Main] for main-thread operations. */
    override val main: CoroutineDispatcher = Dispatchers.Main

    /** Uses [Dispatchers.Default] for CPU-intensive operations. */
    override val default: CoroutineDispatcher = Dispatchers.Default
}
