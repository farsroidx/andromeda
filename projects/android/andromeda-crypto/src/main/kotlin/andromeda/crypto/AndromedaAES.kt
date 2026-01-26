@file:Suppress("unused")

package andromeda.crypto

import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Ultra-secure Hybrid Encryption Module (E2EE-ready) for Android
 *
 * Provides state-of-the-art end-to-end encryption using:
 * - AES-256-CBC with random IV (hardware-backed via Android Keystore)
 * - Fully compatible with minSdk 23
 * - Private keys never leave the secure hardware (impossible to extract)
 *
 * Recommended E2EE flow in a messaging app:
 * 1. On first app launch: `Encryption.initRsaKeyPair()`
 * 2. Encrypt outgoing message: `Encryption.encryptAes(messageBytes)`
 * 3. Share your AES key securely: `Encryption.encryptAesKeyWithServerPublicKey(...)`
 * 4. Decrypt incoming message: `Encryption.decryptAes(encryptedBytes)`
 */
object AndromedaAES {
    private const val USER_AUTHENTICATION_REQUIRED = false // Set true if you want biometric prompt

    private const val KEY_ALIAS = "ir.farsroidx.andromeda.aes_key"
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
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

    fun encrypt(bytes: ByteArray): ByteArray {
        val cipher = AndromedaCipher.instance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        return cipher.iv + cipher.doFinal(bytes)
    }

    fun decrypt(bytes: ByteArray): ByteArray {
        val cipher = AndromedaCipher.instance(TRANSFORMATION)
        val ivSize = cipher.blockSize
        if (bytes.size <= ivSize) throw IllegalArgumentException("Invalid or corrupted encrypted data")
        val iv = bytes.copyOfRange(0, ivSize)
        val encryptedData = bytes.copyOfRange(ivSize, bytes.size)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), IvParameterSpec(iv))
        return cipher.doFinal(encryptedData)
    }

    fun deleteKey() {
        if (isKeyAvailable()) AndromedaKeyStore.deleteKey(KEY_ALIAS)
    }
}
