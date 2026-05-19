@file:Suppress("unused")

package andromeda.ui.core

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Represents a one-shot UI contract used to communicate transient UI actions
 * from domain / presentation layers to the UI layer.
 *
 * ## Why this exists
 * In modern Android architectures, not every UI interaction should be modeled
 * as long-lived state. Some actions are inherently ephemeral and should be
 * consumed once by the UI and then discarded. Examples include:
 *
 * - showing a toast/snackBar/dialog
 * - navigating to another screen
 * - navigating back
 * - triggering a custom one-off action
 *
 * `UiEvent` provides a strongly typed, sealed contract for these cases.
 * Because it is sealed, consumers can handle all supported event types with
 * exhaustive `when` statements, which improves safety and maintainability.
 *
 * ## Event categories
 * This hierarchy is intentionally split into the following groups:
 *
 * ### 1) Notifications
 * Message-driven events intended to be shown to the user, such as:
 * - information messages
 * - success feedback
 * - warnings
 * - errors
 * - debug-only messages
 *
 * Notifications are designed to support:
 * - an optional [UiEvent.Notification.title]
 * - an optional [UiEvent.Notification.message]
 * - a presentation [UiEvent.Notification.type]
 * - arbitrary [UiEvent.Notification.metadata]
 * - display [UiEvent.Notification.duration]
 * - auto-dismiss behavior
 *
 * ### 2) Triggers
 * Generic action events for custom or feature-specific behavior that does not
 * fit into predefined event categories. Triggers are useful when:
 * - you need to invoke a one-off UI side effect
 * - the action is dynamic or module-specific
 * - you want to avoid over-engineering the event model
 *
 * ### 3) Navigation commands
 * Events that instruct the UI to navigate to a destination or navigate up.
 *
 * ## Design notes
 * - This contract is intentionally lightweight and UI-focused.
 * - It is not intended to represent persistent screen state.
 * - Notification events can be queued and consumed sequentially by the UI.
 * - Navigation and trigger events are dispatched immediately.
 * - `DismissNotification` is a dedicated command that lets the UI clear the
 *   currently displayed notification without relying on `null`.
 *
 * ## Usage example
 *
 * ```kotlin
 * UiEventManager.infoNotification(
 *     title = UiText.DynamicString("Done"),
 *     message = UiText.DynamicString("Profile saved successfully")
 * )
 * ```
 *
 * @see [UiEventManager]
 * */
sealed interface UiEvent {
    /**
     * Represents a user-facing notification event.
     *
     * Notifications are one-shot events used to present transient messages to
     * the user. They are commonly rendered as:
     *
     * - toast
     * - snackBar
     * - inline banner
     * - dialog
     * - custom notification card
     *
     * The UI layer decides how to present the event based on [type] and
     * application-specific rules.
     *
     * @property title Optional notification title.
     * @property message Optional notification body content.
     * @property type Visual or behavioral presentation hint for the UI layer.
     * @property metadata Arbitrary payload associated with the notification.
     * @property duration Desired visibility duration in milliseconds.
     * @property autoDismiss Whether the notification should be automatically dismissed after [duration].
     */
    sealed class Notification(
        open val title: UiText?,
        open val message: UiText?,
        open val type: UiEventType?,
        open val metadata: Any?,
        open val duration: Long,
        open val autoDismiss: Boolean,
    ) : UiEvent {
        /** Indicates a general informational message. */
        data class Info(
            override val title: UiText?,
            override val message: UiText?,
            override val type: UiEventType?,
            override val metadata: Any?,
            override val duration: Long,
            override val autoDismiss: Boolean,
        ) : Notification(title, message, type, metadata, duration, autoDismiss)

        /** Indicates a successful operation or positive feedback. */
        data class Success(
            override val title: UiText?,
            override val message: UiText?,
            override val type: UiEventType?,
            override val metadata: Any?,
            override val duration: Long,
            override val autoDismiss: Boolean,
        ) : Notification(title, message, type, metadata, duration, autoDismiss)

        /** Indicates a warning, urging the user to be cautious. */
        data class Warning(
            override val title: UiText?,
            override val message: UiText?,
            override val type: UiEventType?,
            override val metadata: Any?,
            override val duration: Long,
            override val autoDismiss: Boolean,
        ) : Notification(title, message, type, metadata, duration, autoDismiss)

        /** Indicates a failure, error, or an exceptional state that needs attention. */
        data class Error(
            override val title: UiText?,
            override val message: UiText?,
            override val type: UiEventType?,
            override val metadata: Any?,
            override val duration: Long,
            override val autoDismiss: Boolean,
        ) : Notification(title, message, type, metadata, duration, autoDismiss)

        /**
         * Debug-only notification intended for development and diagnostics.
         *
         * The event manager may suppress this event in production depending on
         * its debug configuration.
         */
        data class Debug(
            override val title: UiText?,
            override val message: UiText?,
            override val type: UiEventType?,
            override val metadata: Any?,
            override val duration: Long,
            override val autoDismiss: Boolean,
        ) : Notification(title, message, type, metadata, duration, autoDismiss)

        companion object {
            /**
             * Default notification visibility duration in milliseconds.
             */
            const val DEFAULT_DURATION: Long = 5_000L

            /**
             * Default notification visibility hidden duration in milliseconds.
             */
            const val DISMISS_ANIMATION_DURATION = 500L
        }
    }

    /**
     * Represents a custom one-off action that can be handled by the UI layer.
     *
     * Triggers are useful when a feature needs to send a lightweight command
     * without coupling itself to a specific UI component implementation.
     *
     * **Example use cases**:
     * - opening a picker
     * - requesting a scroll-to-top action
     * - emitting module-specific UI callbacks
     *
     * @property actionId Stable identifier used by the UI layer to route the action.
     * @property metadata Optional payload associated with the action.
     */
    data class Trigger(
        val actionId: String,
        val metadata: Map<String, Any?>,
    ) : UiEvent {
        init {
            require(actionId.isNotBlank()) { "actionId must not be blank." }
        }

        /**
         * Safely retrieves a value from the [metadata] map cast to the inferred type [T].
         *
         * @param key The identifier of the metadata entry.
         * @return The cast value if present and of correct type, or `null` otherwise.
         */
        inline fun <reified T> metadata(key: String): T? = metadata[key] as? T

        /**
         * Retrieves a value from the [metadata] map or falls back to [defaultValue].
         *
         * @param key The identifier of the metadata entry.
         * @param defaultValue The value to return if the key is missing or type casting fails.
         * @return The cast value or the [defaultValue].
         */
        inline fun <reified T> metadata(
            key: String,
            defaultValue: T,
        ): T = metadata[key] as? T ?: defaultValue
    }

    /**
     * Directs the UI layer to navigate to a specific destination, providing fine-grained control
     * over the back stack and state management. Fully compatible with both traditional route-based
     * and modern Type-Safe navigation in Jetpack Compose.
     *
     * `destination` is intentionally typed as [Any] to support both:
     * - traditional string-based routes
     * - type-safe navigation destinations
     *
     * @property destination The target destination. Typed as [Any] to support traditional `String` routes as well as modern Type-Safe serialization objects.
     * @property clearBackStack A convenience shortcut to completely wipe the current back stack upon navigation. Overlaps functionally with [popUpToRoute]; use whichever fits your graph logic better.
     * @property popUpToRoute The specific route to pop up to before navigating, effectively clearing the back stack up to that point.
     * @property inclusive Determines whether the [popUpToRoute] itself should also be popped from the back stack (true) or kept (false).
     * @property launchSingleTop Prevents multiple identical instances of the [destination] from piling up on top of the back stack.
     * @property restoreState Whether this navigation action should restore the state previously saved by a [saveState] operation.
     * @property saveState Whether the state of the destinations being popped should be saved for future restoration.
     */
    data class Navigate(
        val destination: Any,
        val clearBackStack: Boolean = false,
        val popUpToRoute: String? = null,
        val inclusive: Boolean = false,
        val launchSingleTop: Boolean = true,
        val restoreState: Boolean = false,
        val saveState: Boolean = false,
    ) : UiEvent

    /**
     * Instructs the UI layer to navigate back to the previous destination.
     */
    data object NavigateUp : UiEvent

    /**
     * Instructs the UI layer to dismiss the currently visible notification.
     *
     * This is intentionally modeled as a concrete event instead of using `null`
     * so the event stream remains strongly typed and easier to reason about.
     */
    data object DismissNotification : UiEvent
}
