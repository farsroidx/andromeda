@file:Suppress("unused")

package andromeda.crypto

import android.os.Build
import java.util.Base64

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaBase64 {
    /**
     * Encodes a byte array to a Base64 string (URL-safe, no wrapping).
     *
     * @param value Bytes to encode
     * @return Base64-encoded string
     */
    fun encode(value: ByteArray): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(value)
        } else {
            android.util.Base64.encodeToString(value, android.util.Base64.NO_WRAP)
        }

    /**
     * Decodes a Base64 string to a byte array.
     *
     * @param value Base64 string
     * @return Decoded bytes
     */
    fun decode(value: String): ByteArray =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(value)
        } else {
            android.util.Base64.decode(value, android.util.Base64.NO_WRAP)
        }
}
