@file:Suppress("unused")

package ir.farsroidx.andromeda.viewmodel.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import ir.farsroidx.andromeda.viewmodel.AndromedaMviViewModel
import ir.farsroidx.andromeda.viewmodel.contract.AndromedaUiEffect
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Observes [AndromedaUiEffect] emissions from a given [AndromedaMviViewModel] and executes
 * a [callback] on the **Main thread** for each effect.
 *
 * This Composable automatically handles lifecycle:
 * - Starts collecting when the Composable enters composition.
 * - Cancels collection when the Composable leaves' composition.
 * - Calls [ir.farsroidx.andromeda.viewmodel.AndromedaViewModel.onDispose] on disposal.
 *
 * Exception handling is supported via [onException].
 *
 * Example usage:
 * ```
 * ObserveEffects(viewModel = homeViewModel, onException = { logError(it) }) { effect ->
 *     when(effect) {
 *         is HomeEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
 *     }
 * }
 * ```
 *
 * @param viewModel The [AndromedaMviViewModel] whose effects should be observed.
 * @param onException Lambda invoked on any exception during collection. Defaults to logging.
 * @param callback Lambda invoked on each emitted effect on the **Main thread**.
 */
@Composable
fun <T : AndromedaUiEffect> ObserveEffects(
    viewModel: AndromedaMviViewModel<*, *, *, T>,
    onException: (Throwable) -> Unit = {
        Log.e("ObserveEffects", "Effect Error: ${it.message}")
    },
    callback: suspend (T) -> Unit
) {

    LaunchedEffect(viewModel) {

        try {

            viewModel.uiEffects.collect { effect ->

                withContext(Dispatchers.Main) {
                    callback(effect)
                }
            }

        } catch (throwable: Throwable) {

            if (throwable !is CancellationException) {

                withContext(Dispatchers.Main) {

                    onException(throwable)

                }
            }
        }
    }

    DisposableEffect(viewModel) {
        onDispose { viewModel.onDispose() }
    }
}