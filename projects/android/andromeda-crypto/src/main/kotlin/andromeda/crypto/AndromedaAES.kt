@file:Suppress("unused")

package andromeda.crypto

import android.security.keystore.KeyProperties
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

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

    const val KEY_ALIAS = "ir.farsroidx.andromeda.aes_key"
    const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    const val KEY_SIZE = 256
    const val IV_SIZE = 12
    const val TAG_SIZE = 128

    fun getSecretKey(identifier: String? = null): SecretKey =
        AndromedaKeyStore.getSecretKey(alias = getAliasKey(identifier))
            ?: AndromedaKeyStore.initAesSecretKey(
                size = KEY_SIZE,
                alias = getAliasKey(identifier),
                padding = PADDING,
                algorithm = ALGORITHM,
                blockMode = BLOCK_MODE,
                authRequired = USER_AUTHENTICATION_REQUIRED,
            )

    @Suppress("InsecureCryptoUsage", "java:S3329")
    fun encrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val payloadIv = ByteArray(IV_SIZE).also {
            // Secure Random IV generated per encryption operation
            SecureRandom().nextBytes(it)
        }
        // Safe: IV is generated randomly during encryption and extracted from payload
        val spec = GCMParameterSpec(TAG_SIZE, payloadIv)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), spec)
        val encrypted = cipher.doFinal(data)
        // output = IV || CIPHERTEXT || TAG
        return payloadIv + encrypted
    }

    @Suppress("InsecureCryptoUsage", "java:S3329")
    fun decrypt(data: ByteArray): ByteArray {
        if (data.size <= IV_SIZE) throw IllegalArgumentException("Invalid encrypted data")
        val payloadIv = data.copyOfRange(0, IV_SIZE)
        val encrypted = data.copyOfRange(IV_SIZE, data.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        // Safe: IV is generated randomly during encryption and extracted from payload
        val spec = GCMParameterSpec(TAG_SIZE, payloadIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        return cipher.doFinal(encrypted)
    }

    fun deleteKey(identifier: String? = null) = AndromedaKeyStore.deleteKey(alias = getAliasKey(identifier))

    fun deleteKeys() {
        AndromedaKeyStore.getAliases().forEach {
            if (it.startsWith(prefix = KEY_ALIAS)) {
                AndromedaKeyStore.deleteKey(alias = it)
            }
        }
    }

    private fun getAliasKey(identifier: String? = null): String = if (identifier == null) KEY_ALIAS else "${KEY_ALIAS}.$identifier"
}
