@file:Suppress("unused")

package andromeda.ui.core

import andromeda.ui.core.UiEventManager.install
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Central dispatcher for [UiEvent] instances.
 *
 * `UiEventManager` acts as a lightweight event bridge between the domain /
 * presentation layers and the UI layer. It is specifically designed for
 * **one-shot, transient UI actions** rather than persistent screen state.
 *
 * ## Responsibilities
 * This manager supports three primary event flows:
 *
 * ### 1) Notifications
 * Notification events are queued and emitted sequentially. This prevents
 * multiple transient messages from overlapping or replacing each other
 * unexpectedly.
 *
 * - If a notification is already visible, subsequent notifications are queued.
 * - If no notification is visible, the new notification is shown immediately.
 * - Notifications may auto-dismiss after their configured duration.
 *
 * ### 2) Immediate commands
 * Navigation events, triggers, and other non-notification commands are emitted
 * immediately and are not queued.
 *
 * ### 3) Forced notifications
 * A forced notification interrupts the currently visible notification,
 * clears the queue, and displays the new notification as soon as possible.
 *
 * ## Dismissal model
 * The manager uses a dedicated [UiEvent.DismissNotification] event to inform
 * the UI that the current notification should be hidden. This is preferred
 * over `null` because:
 *
 * - the stream remains strongly typed
 * - event handling becomes explicit
 * - consumers do not need null checks for control flow
 *
 * ## Threading and coroutine model
 * - Events are dispatched through a `Channel`
 * - Notification timing is managed on a `CoroutineScope`
 * - A `SupervisorJob` is used so one failed coroutine does not cancel the whole manager
 * - `Dispatchers.Main.immediate` is used because these events are UI-facing
 *
 * ## Important usage notes
 * - This manager is intended to be collected by the UI layer.
 * - The UI layer should react to events exactly once.
 * - `debugNotification()` only emits when debug mode is enabled.
 * - `clear()` dismisses the active notification and optionally discards the queue.
 *
 * ## Example
 *
```kotlin
 * LaunchedEffect(Unit) {
 *     UiEventManager.events.collect { event ->
 *         when (event) {
 *             is UiEvent.Notification -> showNotification(event)
 *             is UiEvent.Navigate     -> navController.navigate(event.destination)
 *             is UiEvent.NavigateUp   -> navController.navigateUp()
 *             is UiEvent.Trigger      -> handleTrigger(event)
 *             is UiEvent.DismissNotification -> hideNotification()
 *         }
 *     }
 * }
 * ```
 *
 * @see [UiEvent]
 */
object UiEventManager {
    private var debugEnabled: Boolean = true

    private val scope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    private val _events = Channel<UiEvent>(capacity = Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    private val notificationQueue = ArrayDeque<UiEvent.Notification>()

    private var currentNotification: UiEvent.Notification? = null
    private var currentToken: Long = 0L

    private var dismissJob: Job? = null
    private var transitionJob: Job? = null

    private val tokenGenerator = AtomicLong(0L)

    /**
     * Configures the manager.
     *
     * @param isDebuggable `true` to allow [UiEvent.Notification.Debug] events to be emitted.
     */
    fun install(isDebuggable: Boolean = true) {
        debugEnabled = isDebuggable
    }

    /**
     * Emits an informational notification.
     *
     * @see UiEvent.Notification.Info
     */
    fun infoNotification(
        title: UiText? = null,
        message: UiText? = null,
        metadata: Any? = null,
        type: UiEventType = UiEventType.NONE,
        duration: Long = UiEvent.Notification.DEFAULT_DURATION,
        autoDismiss: Boolean = true,
        isForce: Boolean = false,
    ) = emitNotification(
        UiEvent.Notification.Info(title, message, type, metadata, duration, autoDismiss),
        isForce,
    )

    /**
     * Emits a success notification.
     *
     * @see UiEvent.Notification.Success
     */
    fun successNotification(
        title: UiText? = null,
        message: UiText? = null,
        metadata: Any? = null,
        type: UiEventType = UiEventType.NONE,
        duration: Long = UiEvent.Notification.DEFAULT_DURATION,
        autoDismiss: Boolean = true,
        isForce: Boolean = false,
    ) = emitNotification(
        UiEvent.Notification.Success(title, message, type, metadata, duration, autoDismiss),
        isForce,
    )

    /**
     * Emits a warning notification.
     *
     * @see UiEvent.Notification.Warning
     */
    fun warningNotification(
        title: UiText? = null,
        message: UiText? = null,
        metadata: Any? = null,
        type: UiEventType = UiEventType.NONE,
        duration: Long = UiEvent.Notification.DEFAULT_DURATION,
        autoDismiss: Boolean = true,
        isForce: Boolean = false,
    ) = emitNotification(
        UiEvent.Notification.Warning(title, message, type, metadata, duration, autoDismiss),
        isForce,
    )

    /**
     * Emits an error notification.
     *
     * @see UiEvent.Notification.Error
     */
    fun errorNotification(
        title: UiText? = null,
        message: UiText? = null,
        metadata: Any? = null,
        type: UiEventType = UiEventType.NONE,
        duration: Long = UiEvent.Notification.DEFAULT_DURATION,
        autoDismiss: Boolean = true,
        isForce: Boolean = false,
    ) = emitNotification(
        UiEvent.Notification.Error(title, message, type, metadata, duration, autoDismiss),
        isForce,
    )

    /**
     * Emits a debug notification.
     *
     * This method is ignored when debug mode is disabled via [install].
     *
     * @see UiEvent.Notification.Debug
     */
    fun debugNotification(
        title: UiText? = null,
        message: UiText? = null,
        metadata: Any? = null,
        type: UiEventType = UiEventType.NONE,
        duration: Long = UiEvent.Notification.DEFAULT_DURATION,
        autoDismiss: Boolean = true,
        isForce: Boolean = false,
    ) {
        if (!debugEnabled) return
        emitNotification(
            UiEvent.Notification.Debug(title, message, type, metadata, duration, autoDismiss),
            isForce,
        )
    }

    /**
     * Emits a custom trigger event immediately.
     *
     * @see UiEvent.Trigger
     */
    fun trigger(event: UiEvent.Trigger) = dispatch(event)

    /**
     * Emits a navigation command immediately.
     *
     * @see UiEvent.Navigate
     */
    fun navigate(event: UiEvent.Navigate) = dispatch(event)

    /**
     * Emits a "navigate up" command immediately.
     *
     * @see UiEvent.NavigateUp
     */
    fun navigateUp() = dispatch(UiEvent.NavigateUp)

    /**
     * Dismisses the current notification.
     *
     * @param clearQueue If `true`, removes all queued notifications as well.
     *
     * The dismissal is explicit and deterministic:
     * - any pending auto-dismiss job is canceled
     * - any pending transition job is canceled
     * - the current notification is cleared
     * - a [UiEvent.DismissNotification] command is emitted
     * - if the queue is not empty, the next notification may be shown later
     */
    fun clear(clearQueue: Boolean = false) {
        dismissJob?.cancel()
        dismissJob = null

        transitionJob?.cancel()
        transitionJob = null

        if (clearQueue) {
            notificationQueue.clear()
        }

        currentNotification = null
        currentToken = 0L

        dispatch(UiEvent.DismissNotification)

        if (!clearQueue) {
            showNextNotification()
        }
    }

    private fun emitNotification(
        notification: UiEvent.Notification,
        isForce: Boolean,
    ) {
        require(notification.duration >= 0L) { "duration must be >= 0" }

        if (isForce) {
            clear(clearQueue = true)
            showNotification(notification)
            return
        }

        if (currentNotification != null) {
            notificationQueue.addLast(notification)
        } else {
            showNotification(notification)
        }
    }

    private fun showNotification(notification: UiEvent.Notification) {
        dismissJob?.cancel()
        transitionJob?.cancel()

        currentNotification = notification

        currentToken = tokenGenerator.incrementAndGet()

        dispatch(notification)

        if (notification.autoDismiss && notification.duration > 0L) {
            val token = currentToken

            dismissJob =
                scope.launch {
                    delay(timeMillis = notification.duration)
                    if (!isActive || currentToken != token) return@launch
                    dismissCurrent()
                }
        }
    }

    private fun dismissCurrent() {
        dismissJob?.cancel()
        dismissJob = null

        currentNotification = null
        currentToken = 0L

        dispatch(UiEvent.DismissNotification)

        transitionJob?.cancel()
        transitionJob =
            scope.launch {
                delay(timeMillis = UiEvent.Notification.DISMISS_ANIMATION_DURATION)
                if (!isActive || currentNotification != null) return@launch
                showNextNotification()
            }
    }

    private fun showNextNotification() {
        val next = notificationQueue.removeFirstOrNull()
        if (next != null) {
            showNotification(next)
        }
    }

    private fun dispatch(event: UiEvent) {
        _events.trySend(event)
    }
}
