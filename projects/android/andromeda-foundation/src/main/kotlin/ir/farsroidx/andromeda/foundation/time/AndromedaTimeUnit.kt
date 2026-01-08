@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.time

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Represents a unit of time with a fixed duration in milliseconds.
 *
 * This sealed hierarchy defines common time units that can be safely used
 * for time calculations, delays, scheduling, and conversions.
 *
 * Each implementation holds its duration in milliseconds via [millis].
 * Utility properties can be derived from this base value.
 *
 * Available units:
 * - [Seconds]
 * - [Minutes]
 * - [Hour]
 * - [Day]
 * - [Week]
 * - [Month]
 * - [Year]
 *
 * @property millis Duration of the time unit in milliseconds.
 */
sealed class AndromedaTimeUnit(
    val millis: Long,
) {
    /**
     * Time unit representing seconds.
     *
     * 1 second = 1,000 milliseconds.
     */
    data object Seconds : AndromedaTimeUnit(1_000)

    /**
     * Time unit representing minutes.
     *
     * 1 minute = 60 seconds = 60,000 milliseconds.
     */
    data object Minutes : AndromedaTimeUnit(60_000)

    /**
     * Time unit representing hours.
     *
     * 1 hour = 60 minutes = 3,600,000 milliseconds.
     */
    data object Hour : AndromedaTimeUnit(3_600_000)

    /**
     * Time unit representing days.
     *
     * 1 day = 24 hours = 86,400,000 milliseconds.
     */
    data object Day : AndromedaTimeUnit(86_400_000)

    /**
     * Time unit representing weeks.
     *
     * 1 week = 7 days = 604,800,000 milliseconds.
     */
    data object Week : AndromedaTimeUnit(604_800_000)

    /**
     * Time unit representing months.
     *
     * This value is based on a fixed 210-day approximation.
     * It is intended for relative or logical time calculations,
     * not for calendar-accurate date operations.
     */
    data object Month : AndromedaTimeUnit(18_144_000_000)

    /**
     * Time unit representing years.
     *
     * This value is based on a fixed 12-month approximation.
     * Suitable for long-duration estimations and non-calendar logic.
     */
    data object Year : AndromedaTimeUnit(217_728_000_000)

    /**
     * Returns the duration of this unit in seconds.
     */
    val seconds: Long
        get() = millis / 1_000
}
