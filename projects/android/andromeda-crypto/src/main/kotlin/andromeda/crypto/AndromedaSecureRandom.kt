@file:Suppress("unused")

package andromeda.crypto

import java.security.SecureRandom

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaSecureRandom {
    private val secureRandom = SecureRandom()

    fun nextBytes(length: Int): ByteArray {
        val bytes = ByteArray(length)
        secureRandom.nextBytes(bytes)
        return bytes
    }

    fun nextHexString(length: Int): String = nextBytes(length / 2).joinToString("") { "%02x".format(it) }
}
