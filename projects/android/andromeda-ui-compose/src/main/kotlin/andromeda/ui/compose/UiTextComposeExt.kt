@file:Suppress("unused")

package andromeda.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import andromeda.ui.core.UiText

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Resolves the text into a displayable [String] using the current [LocalContext].
 * Designed for use inside Composable functions.
 *
 * @return The resolved text as a [String].
 */
@Composable
fun UiText.asString(): String = this.asString(context = LocalContext.current)