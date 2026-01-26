@file:Suppress("unused")

package andromeda.foundation.ktx

import andromeda.foundation.datetime.AndromedaPersianDate
import andromeda.foundation.datetime.AndromedaPersianDate.PersianDateModel

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Returns the current system time as a Jalali [PersianDateModel].
 *
 * Usage:
 * ```
 * val now = persianDate()
 * // now: 1404/01/01
 * ```
 *
 * @see persianDateTime
 */
fun persianDate(pattern: String = "yyyy/MM/dd"): String = AndromedaPersianDate.now(pattern)

/**
 * Returns the current system time as a Jalali [PersianDateModel].
 *
 * Usage:
 * ```
 * val now = persianDateTime()
 * // now: 1404/01/01 10:00:48
 * ```
 *
 * @see persianDate
 */
fun persianDateTime(pattern: String = "yyyy/MM/dd HH:mm:ss"): String = AndromedaPersianDate.now(pattern)

/**
 * Converts this Unix timestamp to a formatted Jalali (Persian) date string.
 *
 * This extension uses [AndromedaPersianDate] internally to:
 * 1. Convert the timestamp (UTC) to a Jalali date-time
 * 2. Format the result using the provided pattern
 *
 * Supported pattern tokens:
 * - `yyyy` → year
 * - `MM`   → month
 * - `dd`   → day
 *
 * @param pattern output date format (default: `yyyy/MM/dd`)
 * @return formatted Jalali date string
 *
 * @receiver Unix timestamp in milliseconds (UTC)
 */
fun Long?.toPersianDate(pattern: String = "yyyy/MM/dd"): String =
    if (this == null) {
        "----/--/--"
    } else {
        val dateTime = AndromedaPersianDate.fromTimestamp(this)
        AndromedaPersianDate.format(dateTime, pattern)
    }

/**
 * Converts this Unix timestamp to a formatted Jalali (Persian) date-time string.
 *
 * This is a convenience extension for cases where both date and time
 * components are required in Jalali calendar format.
 *
 * Supported pattern tokens:
 * - `yyyy` → year
 * - `MM`   → month
 * - `dd`   → day
 * - `HH`   → hour (24h)
 * - `mm`   → minute
 * - `ss`   → second
 *
 * @param pattern output date-time format (default: `yyyy/MM/dd HH:mm:ss`)
 * @return formatted Jalali date-time string
 *
 * @receiver Unix timestamp in milliseconds (UTC)
 */
fun Long?.toPersianDateTime(pattern: String = "yyyy/MM/dd HH:mm:ss"): String =
    if (this == null) {
        "----/--/-- --:--:--"
    } else {
        val dateTime = AndromedaPersianDate.fromTimestamp(this)
        AndromedaPersianDate.format(dateTime, pattern)
    }

/**
 * Converts this Unix timestamp to a [PersianDateModel].
 *
 * Unlike formatting helpers, this extension exposes the raw Jalali
 * date-time components for further processing, comparison,
 * or custom formatting.
 *
 * @return Jalali date-time model
 *
 * @receiver Unix timestamp in milliseconds (UTC)
 */
fun Long.toPersianDateModel(): PersianDateModel = AndromedaPersianDate.fromTimestamp(this)

/**
 * Converts this Jalali date-time model into a Unix timestamp (UTC).
 *
 * The conversion accounts for:
 * - Jalali leap years
 * - Julian Day Number mapping
 * - Iran standard time and historical daylight saving rules
 *
 * @return Unix timestamp in milliseconds (UTC)
 */
fun PersianDateModel.timestamp(): Long = AndromedaPersianDate.toTimestamp(this)

/**
 * Creates a copy of this Jalali date with time set to 00:00:00.
 */
fun PersianDateModel.atStartOfDay(): PersianDateModel = copy(hour = 0, minute = 0, second = 0)

/**
 * Creates a copy of this Jalali date with time set to 23:59:59.
 */
fun PersianDateModel.atEndOfDay(): PersianDateModel = copy(hour = 23, minute = 59, second = 59)

/**
 * Checks whether this Jalali date-time occurs before [other].
 */
fun PersianDateModel.isBefore(other: PersianDateModel): Boolean = this.timestamp() < other.timestamp()

/**
 * Checks whether this Jalali date-time occurs after [other].
 */
fun PersianDateModel.isAfter(other: PersianDateModel): Boolean = this.timestamp() > other.timestamp()

/**
 * Returns a new Jalali date by adding the given number of days to this date.
 *
 * Usage:
 * ```
 * val nextWeek = today + 7
 * ```
 *
 * @receiver base Jalali date
 * @param days number of days to add (can be negative)
 */
operator fun PersianDateModel.plus(days: Int): PersianDateModel {
    val newTimestamp = this.timestamp() + (days * 86_400_000L)
    return AndromedaPersianDate.fromTimestamp(newTimestamp)
}

/**
 * Returns a new Jalali date by subtracting the given number of days from this date.
 *
 * Usage:
 * ```
 * val yesterday = today - 1
 * ```
 *
 * @receiver base Jalali date
 * @param days number of days to subtract
 */
operator fun PersianDateModel.minus(days: Int): PersianDateModel {
    val newTimestamp = this.timestamp() - (days * 86_400_000L)
    return AndromedaPersianDate.fromTimestamp(newTimestamp)
}

/**
 * Returns a new Jalali date after adding [days] days to this date.
 *
 * Usage:
 * ```
 * val dueDate = today addDays 10
 * ```
 */
infix fun PersianDateModel.addDays(days: Int): PersianDateModel = this + days

/**
 * Returns a new Jalali date after subtracting [days] days from this date.
 *
 * Usage:
 * ```
 * val startDate = today minusDays 3
 * ```
 */
infix fun PersianDateModel.minusDays(days: Int): PersianDateModel = this - days

/**
 * Calculates the number of days from this Jalali date until [other].
 *
 * The calculation is normalized to the start of day (00:00)
 * to avoid partial-day side effects.
 *
 * Usage:
 * ```
 * val days = today daysUntil endDate
 * ```
 *
 * @receiver starting Jalali date
 * @param other target Jalali date
 * @return number of full days between the two dates
 */
infix fun PersianDateModel.daysUntil(other: PersianDateModel): Long {
    val start = this.atStartOfDay().timestamp()
    val end = other.atStartOfDay().timestamp()
    return (end - start) / 86_400_000L
}
