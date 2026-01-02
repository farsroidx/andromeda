@file:Suppress("unused")

package ir.farsroidx.andromeda.koin

import org.koin.java.KoinJavaComponent

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * A generic function that lazily provides an instance of a given type [T] using Koin for dependency injection.
 * It is thread-safe and initializes the instance only when it's first accessed.
 *
 * @param T The type of the instance to be provided.
 * @return A lazy-initialized [T] instance, thread-safe using `LazyThreadSafetyMode.SYNCHRONIZED`.
 */
inline fun <reified T> provide(): Lazy<T> {

    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {

        // Fetch the instance of type [T] from Koin using KoinJavaComponent.

        KoinJavaComponent.get(T::class.java, null, null)
    }
}