@file:Suppress("unused")

package andromeda.crypto

import java.security.MessageDigest

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaSHA {
    /**
     * Computes SHA-1 hash of the input data.
     *
     * @param data Input data to hash
     * @return Hex-encoded SHA-1 hash (40 characters)
     */
    fun sha1(data: ByteArray): String = computeHash(data, "SHA-1")

    /**
     * Computes SHA-224 hash of the input data.
     *
     * @param data Input data to hash
     * @return Hex-encoded SHA-224 hash (56 characters)
     */
    fun sha224(data: ByteArray): String = computeHash(data, "SHA-224")

    /**
     * Computes SHA-256 hash of the input data.
     *
     * @param data Input data to hash
     * @return Hex-encoded SHA-256 hash (64 characters)
     */
    fun sha256(data: ByteArray): String = computeHash(data, "SHA-256")

    /**
     * Computes SHA-384 hash of the input data.
     *
     * @param data Input data to hash
     * @return Hex-encoded SHA-384 hash (96 characters)
     */
    fun sha384(data: ByteArray): String = computeHash(data, "SHA-384")

    /**
     * Computes SHA-512 hash of the input data.
     *
     * @param data Input data to hash
     * @return Hex-encoded SHA-512 hash (128 characters)
     */
    fun sha512(data: ByteArray): String = computeHash(data, "SHA-512")

    private fun computeHash(
        data: ByteArray,
        algorithm: String,
    ): String {
        val md = MessageDigest.getInstance(algorithm)
        val digest = md.digest(data)
        return digest.joinToString("") { "%02x".format(it) }
    }
}
