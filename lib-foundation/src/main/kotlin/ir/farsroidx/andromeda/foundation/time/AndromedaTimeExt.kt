@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.time

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Calculates the expiration timestamp based on the current system time.
 *
 * The expiration time is calculated as:
 * currentTimeMillis + (value × unit.millis)
 *
 * @return Expiration time in milliseconds, or -1L if the time instance is null.
 */
fun AndromedaTime?.asExpireTime(): Long = if (this != null) System.currentTimeMillis() + (this.value * this.unit.millis) else -1

/**
 * Checks whether this timestamp has expired based on the current system time.
 *
 * @return true if the timestamp is invalid or already expired, false otherwise.
 */
fun Long?.isExpired(): Boolean = (this != null && this > 0 && this < System.currentTimeMillis())
