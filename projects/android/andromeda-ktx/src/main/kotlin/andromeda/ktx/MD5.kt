@file:Suppress("unused")

package andromeda.ktx

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

val String.md5: String
    get() {
        return try {
            // Get MD5 MessageDigest instance
            val digest = MessageDigest.getInstance("MD5")

            // Update digest with string bytes (using UTF-8 encoding)
            digest.update(this.toByteArray(Charsets.UTF_8))

            // Calculate the hash
            val hashBytes = digest.digest()

            // Convert byte array to hexadecimal string
            BigInteger(1, hashBytes).toString(16).padStart(32, '0')
        } catch (e: NoSuchAlgorithmException) {
            // MD5 should be available on all Java platforms, but handle gracefully
            ""
        }
    }

val String.md5Optimized: String
    get() {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))

            val hexString = StringBuilder(32)

            for (byte in bytes) {
                val hex = (byte.toInt() and 0xFF).toString(16)
                if (hex.length == 1) {
                    hexString.append('0')
                }
                hexString.append(hex)
            }

            hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

fun String.md5WithSalt(salt: String): String = "$this$salt".md5

fun String.verifyMd5(expectedHash: String): Boolean = this.md5.equals(expectedHash, ignoreCase = true)

val String.sha256: String
    get() {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
            BigInteger(1, bytes).toString(16).padStart(64, '0')
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }
