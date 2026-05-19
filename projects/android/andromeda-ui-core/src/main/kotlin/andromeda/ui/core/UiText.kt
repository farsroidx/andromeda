@file:Suppress("unused")

package andromeda.ui.core

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Represents any kind of UI text that can be displayed in your app,
 * supporting plain strings, string resources with arguments, plural forms, and empty states.
 *
 * This abstraction allows you to keep text resolution logic inside the ViewModel or domain layer,
 * and only resolve the string when it’s actually needed by the UI (e.g., in Compose or Views).
 * It also supports nesting [UiText] instances within formatting arguments.
 */
sealed interface UiText {
    /**
     * Represents an empty text state. Safely resolves to an empty string.
     */
    data object Empty : UiText

    /**
     * A dynamic (plain) text that doesn’t rely on Android resources.
     *
     * @property value The literal string to display.
     */
    data class Dynamic(
        val value: String,
    ) : UiText

    /**
     * A string resource-based text, optionally containing formatting arguments.
     *
     * Example usage in `strings.xml`:
     * ```
     * <string name="welcome_message">Welcome, %1$s!</string>
     * ```
     *
     * @property id The string resource ID to be resolved via [Context.getString].
     * @property args Optional list of arguments ([Any]) to replace format placeholders.
     */
    data class StringResource(
        @StringRes val id: Int,
        val args: List<Any> = listOf(),
    ) : UiText

    /**
     * A plural resource-based text that handles singular and plural forms properly.
     *
     * Example usage in `strings.xml`:
     * ```
     * <plurals name="items_count">
     *     <item quantity="one">%d item</item>
     *     <item quantity="other">%d items</item>
     * </plurals>
     * ```
     *
     * @property id The plural resource ID to be resolved via [Context.getResources].
     * @property quantity The count used to determine which plural form to use.
     * @property args Optional list of arguments ([Any]) to replace format placeholders.
     */
    data class Plural(
        @PluralsRes val id: Int,
        val quantity: Int,
        val args: List<Any> = listOf(),
    ) : UiText

    /**
     * Resolves the text into a displayable [String] using the provided [Context].
     * Safe to call from ViewModel-independent or testable components.
     *
     * @param context The Android [Context] used to access string resources.
     * @return The resolved text as a [String].
     */
    fun asString(context: Context): String =
        when (this) {
            is Empty -> {
                ""
            }

            is Dynamic -> {
                value
            }

            is StringResource -> {
                with(receiver = context) {
                    getString(id, *handleArguments(args))
                }
            }

            is Plural -> {
                with(receiver = context) {
                    resources.getQuantityString(id, quantity, *handleArguments(args))
                }
            }
        }

    /**
     * Maps a list of generic arguments ([Any]) into an array suitable for [String.format]
     * or Android’s resource formatting system. Automatically resolves nested [UiText] instances.
     *
     * @param args The list of arguments used to populate placeholders.
     * @return An array of formatted arguments ready for string resolution.
     */
    private fun Context.handleArguments(args: List<Any>): Array<Any> =
        args.map { arg -> if (arg is UiText) arg.asString(context = this) else arg }.toTypedArray()
}
