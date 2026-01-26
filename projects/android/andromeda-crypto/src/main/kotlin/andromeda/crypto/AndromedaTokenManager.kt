@file:Suppress("unused")

package andromeda.crypto

import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaTokenManager {
    private const val USER_AUTHENTICATION_REQUIRED = false // Set true if you want biometric prompt

    private const val KEY_ALIAS = "ir.farsroidx.andromeda.token_key"
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$ALGORITHM/${BLOCK_MODE}/$PADDING"
    private const val KEY_SIZE = 256

    fun isKeyAvailable(): Boolean = AndromedaKeyStore.isKeyAvailable(KEY_ALIAS)

    private fun getSecretKey(): SecretKey =
        AndromedaKeyStore.getSecretKey(KEY_ALIAS)
            ?: AndromedaKeyStore.initSecretKey(
                size = KEY_SIZE,
                alias = KEY_ALIAS,
                padding = PADDING,
                algorithm = ALGORITHM,
                blockMode = BLOCK_MODE,
                authRequired = USER_AUTHENTICATION_REQUIRED,
            )

    fun encrypt(token: String): ByteArray {
        val cipher = AndromedaCipher.instance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        return cipher.iv + cipher.doFinal(token.toByteArray())
    }

    fun decrypt(encryptedToken: ByteArray): String {
        val cipher = AndromedaCipher.instance(TRANSFORMATION)
        val ivSize = cipher.blockSize
        if (encryptedToken.size <= ivSize) throw IllegalArgumentException("Invalid or corrupted token")
        val iv = encryptedToken.copyOfRange(0, ivSize)
        val encryptedData = encryptedToken.copyOfRange(ivSize, encryptedToken.size)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), IvParameterSpec(iv))
        return String(cipher.doFinal(encryptedData))
    }

    fun deleteKey() {
        if (isKeyAvailable()) AndromedaKeyStore.deleteKey(KEY_ALIAS)
    }
}
