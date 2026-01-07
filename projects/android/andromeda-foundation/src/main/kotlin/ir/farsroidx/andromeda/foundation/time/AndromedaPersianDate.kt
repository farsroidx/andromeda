@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.time

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * High-precision Persian (Jalali) date and time utility.
 *
 * This object provides **bidirectional conversion** between Unix timestamps
 * (milliseconds since 1970-01-01T00:00:00Z) and Jalali (Persian) calendar dates,
 * with explicit handling of Iran standard time and historical daylight saving rules.
 *
 * ## Features
 * - Convert current system time to Jalali date/time
 * - Convert Unix timestamps to Jalali date/time
 * - Convert Jalali date/time back to Unix timestamp
 * - Pattern-based formatting without relying on `java.time`
 * - Accurate Julian Day Number (JDN) based calculations
 *
 * ## Time Zone Notes
 * - Standard Time: UTC +03:30
 * - Daylight Saving Time (DST): UTC +04:30 (applied only before year 1402)
 *
 * This implementation is **platform-independent**, allocation-free,
 * and suitable for Android, JVM, and multiplatform use cases.
 */
object AndromedaPersianDate {
    /** Iran Standard Time offset (UTC +03:30) in milliseconds. */
    private const val IRAN_OFFSET_STD = 12600000L // UTC +3:30

    /** Iran Daylight Saving Time offset (UTC +04:30) in milliseconds. */
    private const val IRAN_OFFSET_DST = 16200000L // UTC +4:30

    /** Number of milliseconds in a single day. */
    private const val MILLIS_IN_DAY = 86400000L

    /**
     * Returns the current system time as a Jalali date-time model.
     *
     * @return current date and time in Persian (Jalali) calendar
     */
    fun now(): PersianDateTimeModel = fromTimestamp(System.currentTimeMillis())

    /**
     * Returns the current Jalali date-time formatted using the provided pattern.
     *
     * Supported pattern tokens:
     * - `yyyy` → year
     * - `MM`   → month
     * - `dd`   → day
     * - `HH`   → hour (24h)
     * - `mm`   → minute
     * - `ss`   → second
     *
     * @param pattern formatting pattern (default: `yyyy/MM/dd HH:mm:ss`)
     * @return formatted Jalali date-time string
     */
    fun now(pattern: String = "yyyy/MM/dd HH:mm:ss"): String = format(dt = now(), pattern = pattern)

    /**
     * Converts a Unix timestamp (UTC) to a Jalali date-time.
     *
     * The conversion:
     * 1. Determines whether the timestamp falls under Iran DST
     * 2. Applies the correct time offset
     * 3. Converts the result to Julian Day Number (JDN)
     * 4. Maps the JDN to Jalali calendar components
     *
     * @param timestamp Unix timestamp in milliseconds (UTC)
     * @return corresponding Jalali date-time
     */
    fun fromTimestamp(timestamp: Long): PersianDateTimeModel {
        val isDst = isIranDaylightSaving(timestamp)

        val offset = if (isDst) IRAN_OFFSET_DST else IRAN_OFFSET_STD

        val localMillis = timestamp + offset

        val totalSeconds = localMillis / 1000L
        val seconds = (totalSeconds % 60).toInt()
        val totalMinutes = totalSeconds / 60
        val minutes = (totalMinutes % 60).toInt()
        val totalHours = totalMinutes / 60
        val hours = (totalHours % 24).toInt()
        val daysSinceEpoch = totalHours / 24

        // Julian Day Number for 1970-01-01 is 2440587
        return calculateJalaliFromJdn(daysSinceEpoch + 2440587, hours, minutes, seconds)
    }

    /**
     * Converts a Jalali date-time into a Unix timestamp (UTC).
     *
     * This method:
     * - Converts Jalali date to Julian Day Number
     * - Calculates local milliseconds
     * - Determines DST status
     * - Removes the appropriate Iran time offset
     *
     * @param dt Jalali date-time model
     * @return Unix timestamp in milliseconds (UTC)
     */
    fun toTimestamp(dt: PersianDateTimeModel): Long {
        val jdn = calculateJdnFromJalali(dt.year, dt.month, dt.day)

        val daysSinceEpoch = jdn - 2440588

        val timeMillis = (dt.hour * 3600L + dt.minute * 60L + dt.second) * 1000L
        val localTimestamp = (daysSinceEpoch * MILLIS_IN_DAY) + timeMillis

        // Determine offset by checking DST for the calculated local time
        val isDst = isIranDaylightSaving(localTimestamp - IRAN_OFFSET_STD)
        val offset = if (isDst) IRAN_OFFSET_DST else IRAN_OFFSET_STD

        return localTimestamp - offset
    }

    /**
     * Formats a Jalali date-time using a lightweight token replacement strategy.
     *
     * This method intentionally avoids `SimpleDateFormat`
     * and `java.time` for performance and portability.
     *
     * @param dt Jalali date-time model
     * @param pattern formatting pattern
     * @return formatted date-time string
     */
    fun format(
        dt: PersianDateTimeModel,
        pattern: String,
    ): String =
        pattern
            .replace("yyyy", "%04d".format(dt.year))
            .replace("MM", "%02d".format(dt.month))
            .replace("dd", "%02d".format(dt.day))
            .replace("HH", "%02d".format(dt.hour))
            .replace("mm", "%02d".format(dt.minute))
            .replace("ss", "%02d".format(dt.second))

    /**
     * Determines whether a given timestamp falls under Iran daylight saving time.
     *
     * DST rules:
     * - Applied only for years before 1402
     * - Active roughly from Farvardin 2 to Shahrivar 30
     *
     * @param timestamp Unix timestamp in milliseconds (UTC)
     * @return `true` if DST is active, otherwise `false`
     */
    private fun isIranDaylightSaving(timestamp: Long): Boolean {
        val approxJdn = (timestamp + IRAN_OFFSET_STD) / MILLIS_IN_DAY + 2440588

        val date = calculateJalaliFromJdn(approxJdn, 0, 0, 0)

        if (date.year >= 1402) return false

        return when (date.month) {
            in 2..6 -> true
            1 -> date.day > 1
            else -> false
        }
    }

    /**
     * Converts a Julian Day Number (JDN) to Jalali date components.
     *
     * This method follows the official Jalali calendar algorithm
     * and guarantees correct leap year handling.
     *
     * @param jdn Julian Day Number
     * @param h hour
     * @param m minute
     * @param s second
     * @return Jalali date-time model
     */
    private fun calculateJalaliFromJdn(
        jdn: Long,
        h: Int,
        m: Int,
        s: Int,
    ): PersianDateTimeModel {
        val dEpoch = jdn - 2121446L
        val cycle = dEpoch / 1029983L
        val cYear = dEpoch % 1029983L

        val yCycle =
            if (cYear == 1029982L) {
                2820L
            } else {
                val aux1 = cYear / 366L
                val aux2 = cYear % 366L
                ((2134L * aux1 + 2816L * aux2 + 2815L) / 1028522L) + aux1 + 1
            }

        val jy = yCycle + 2820L * cycle + 474L
        val year = if (jy > 0) jy else jy - 1

        val yDay = jdn - calculateJdnFromJalali(year.toInt(), 1, 1) + 1

        val month =
            if (yDay <= 186) {
                ((yDay - 1) / 31 + 1).toInt()
            } else {
                ((yDay - 187) / 30 + 7).toInt()
            }

        val day =
            if (yDay <= 186) {
                ((yDay - 1) % 31 + 1).toInt()
            } else {
                ((yDay - 187) % 30 + 1).toInt()
            }

        return PersianDateTimeModel(
            year = year.toInt(),
            month = month,
            day = day,
            hour = h,
            minute = m,
            second = s,
        )
    }

    private fun calculateJalaliFromJdn2(
        jdn: Long,
        h: Int,
        m: Int,
        s: Int,
    ): PersianDateTimeModel {
        val dep = jdn - 2121445L
        val cycle = dep / 12053L
        var d1 = dep % 12053L
        var jy = 979L + 33L * cycle + 4L * (d1 / 1461L)

        d1 %= 1461L

        if (d1 >= 366L) {
            jy += ((d1 - 1L) / 365L)
            d1 = (d1 - 1L) % 365L
        }

        val jm = if (d1 < 186L) 1 + (d1 / 31L).toInt() else 7 + ((d1 - 186L) / 30L).toInt()
        val jd = if (d1 < 186L) 1 + (d1 % 31L).toInt() else 1 + ((d1 - 186L) % 30L).toInt()

        return PersianDateTimeModel(
            year = jy.toInt(),
            month = jm,
            day = jd,
            hour = h,
            minute = m,
            second = s,
        )
    }

    /**
     * Converts a Jalali date to its corresponding Julian Day Number (JDN).
     *
     * @param jy Jalali year
     * @param jm Jalali month (1-12)
     * @param jd Jalali day
     * @return Julian Day Number
     */
    private fun calculateJdnFromJalali(
        jy: Int,
        jm: Int,
        jd: Int,
    ): Long {
        val epBase = jy - if (jy >= 0) 474 else 473
        val epYear = 474 + (epBase % 2820)

        val monthDays =
            if (jm <= 7) {
                (jm - 1) * 31
            } else {
                (jm - 1) * 30 + 6
            }

        return jd +
            monthDays +
            ((epYear * 682 - 110) / 2816) +
            (epYear - 1) * 365 +
            (epBase / 2820) * 1029983L +
            1948320L
    }

    /**
     * Immutable Jalali date-time data holder.
     *
     * @property year Jalali year (e.g., 1404)
     * @property month Jalali month (1-12)
     * @property day Day of month (1-31)
     * @property hour Hour of day (0-23)
     * @property minute Minute (0-59)
     * @property second Second (0-59)
     */
    data class PersianDateTimeModel(
        val year: Int,
        val month: Int,
        val day: Int,
        val hour: Int = 0,
        val minute: Int = 0,
        val second: Int = 0,
    )
}
