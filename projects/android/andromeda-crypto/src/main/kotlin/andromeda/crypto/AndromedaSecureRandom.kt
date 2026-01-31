@file:Suppress("unused")

package andromeda.crypto

import java.security.SecureRandom

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * [AndromedaSecureRandom] provides a secure random byte generator and hex string generator.
 *
 * This utility wraps `java.security.SecureRandom` to produce cryptographically strong random data.
 * It is suitable for generating:
 * - Salts for password hashing
 * - AES keys or IVs
 * - Nonce's
 * - Random tokens or identifiers
 *
 * ## Usage
 * ```kotlin
 * val randomBytes = AndromedaSecureRandom.nextBytes(16) // 16 random bytes
 * val randomHex = AndromedaSecureRandom.nextHexString(32) // 32-character hex string
 * ```
 *
 * ## Security Notes
 * - Uses `SecureRandom`, which is cryptographically secure.
 * - For hex strings, the requested length is in characters; internally, half as many bytes are generated.
 */
object AndromedaSecureRandom {
    private val secureRandom = SecureRandom()

    /**
     * Generates a secure random byte array of the specified length.
     *
     * @param length Number of bytes to generate
     * @return Random bytes
     */
    fun nextBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        secureRandom.nextBytes(bytes)
        return bytes
    }

    /**
     * Generates a secure random hexadecimal string of the specified length.
     *
     * @param length Desired length of the hex string (must be even)
     * @return Hexadecimal string representing random bytes
     */
    fun nextHexString(length: Int): String = nextBytes(length / 2).joinToString("") { "%02x".format(it) }
}
