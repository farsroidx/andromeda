@file:Suppress("SameParameterValue", "SpellCheckingInspection", "unused")

package andromeda.crypto

import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaPasswordHash {
    private const val ITERATIONS = 10000
    private const val KEY_LENGTH = 256
    private const val SALT_SIZE = 16

    fun hashPassword(password: String): String {
        val salt = ByteArray(SALT_SIZE).apply { SecureRandom().nextBytes(this) }
        val hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        return AndromedaBase64.encode(salt) + ":" + AndromedaBase64.encode(hash)
    }

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
