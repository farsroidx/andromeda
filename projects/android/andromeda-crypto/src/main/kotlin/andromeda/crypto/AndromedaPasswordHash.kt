@file:Suppress("SameParameterValue", "SpellCheckingInspection", "unused")

package andromeda.crypto

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * [AndromedaPasswordHash] provides secure password hashing and verification
 * using PBKDF2 with HMAC-SHA256.
 *
 * ## Features
 * - Generates a random 16-byte salt for each password.
 * - Uses 10,000 iterations for PBKDF2 to slow down brute-force attacks.
 * - Produces a 256-bit derived key as the password hash.
 * - Stores the salt and hash in Base64 format, separated by `":"`.
 *
 * ## Usage
 * ```kotlin
 * // Hash a password
 * val hashed = AndromedaPasswordHash.hashPassword("mySecretPassword")
 *
 * // Verify a password
 * val isValid = AndromedaPasswordHash.verifyPassword("mySecretPassword", hashed)
 * println(isValid) // true
 * ```
 *
 * ## Security Notes
 * - Never store raw passwords; always use this hashing function.
 * - The salt ensures that identical passwords produce different hashes.
 * - Iteration count and key length are configurable via constants in the class.
 * - The resulting hash string can be safely stored in databases or secure storage.
 */
object AndromedaPasswordHash {
    private const val ITERATIONS = 10000
    private const val KEY_LENGTH = 256
    private const val SALT_SIZE = 16

    /**
     * Generates a secure hash for the given password.
     *
     * @param password Plaintext password to hash.
     * @return Base64-encoded salt and hash in the format `salt:hash`.
     */
    fun hashPassword(password: String): String {
        val salt = ByteArray(SALT_SIZE).apply { SecureRandom().nextBytes(this) }
        val hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        return AndromedaBase64.encode(salt) + ":" + AndromedaBase64.encode(hash)
    }

    /**
     * Verifies a password against a stored hash.
     *
     * @param password Plaintext password to check.
     * @param storedHash Stored password hash in the format `salt:hash`.
     * @return `true` if the password is correct, `false` otherwise.
     */
    fun verifyPassword(
        password: String,
        storedHash: String,
    ): Boolean {
        val (saltBase64, hashBase64) = storedHash.split(":")
        val salt = AndromedaBase64.decode(saltBase64)
        val hash = AndromedaBase64.decode(hashBase64)
        val computedHash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        return computedHash.contentEquals(hash)
    }

    /**
     * PBKDF2-HMAC-SHA256 key derivation.
     *
     * @param password Password characters
     * @param salt Random salt bytes
     * @param iterations Number of iterations
     * @param keyLength Key length in bits
     * @return Derived key as byte array
     */
    private fun pbkdf2(
        password: CharArray,
        salt: ByteArray,
        iterations: Int,
        keyLength: Int,
    ): ByteArray {
        val spec = PBEKeySpec(password, salt, iterations, keyLength)
        val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return skf.generateSecret(spec).encoded
    }
}
