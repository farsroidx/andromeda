@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.time

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Data class representing a time duration with a value and a unit.
 * The value represents the amount of time and the unit specifies the time unit (e.g., seconds, minutes, etc.).
 *
 * @property value The numeric value of the time duration.
 * @property unit The unit of the time duration (e.g., seconds, minutes).
 */
data class AndromedaTime(val value: Int, val unit: AndromedaTimeUnit)