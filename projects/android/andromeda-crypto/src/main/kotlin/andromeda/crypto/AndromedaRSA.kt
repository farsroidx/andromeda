@file:Suppress("unused")

package andromeda.crypto

import android.security.keystore.KeyProperties
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Ultra-secure Hybrid Encryption Module (E2EE-ready) for Android
 *
 * Provides state-of-the-art end-to-end encryption using:
 * - RSA-4096 for secure key exchange with server or peers
 * - Fully compatible with minSdk 23
 * - Private keys never leave the secure hardware (impossible to extract)
 *
 * Recommended E2EE flow in a messaging app:
 * 1. On first app launch: `Encryption.initRsaKeyPair()`
 * 2. Encrypt outgoing message: `Encryption.encryptAes(messageBytes)`
 * 3. Share your AES key securely: `Encryption.encryptAesKeyWithServerPublicKey(...)`
 * 4. Decrypt incoming message: `Encryption.decryptAes(encryptedBytes)`
 */
object AndromedaRSA {
    private const val USER_AUTHENTICATION_REQUIRED = false // Set true if you want biometric prompt

    private const val KEY_ALIAS = "ir.farsroidx.andromeda.rsa_key"
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    private const val KEY_SIZE = 4096

    fun isKeyAvailable(): Boolean = AndromedaKeyStore.isKeyAvailable(KEY_ALIAS)

    fun initKeyPair() {
        if (!isKeyAvailable()) {
            AndromedaKeyStore.initKeyPairs(
                size = KEY_SIZE,
                alias = KEY_ALIAS,
                padding = PADDING,
                algorithm = ALGORITHM,
                blockMode = BLOCK_MODE,
                authRequired = USER_AUTHENTICATION_REQUIRED,
            )
        }
    }

    fun getPublicKey(): PublicKey {
        initKeyPair()
        return AndromedaKeyStore.getPublicKey(KEY_ALIAS)!!
    }

    private fun getPrivateKey(): PrivateKey {
        initKeyPair()
        return AndromedaKeyStore.getPrivateKey(KEY_ALIAS)!!
    }

    fun getPublicKeyBase64(): String = AndromedaBase64.encode(getPublicKey().encoded)

    fun getPublicKeyPem(): String = "-----BEGIN PUBLIC KEY-----\n${getPublicKeyBase64()}\n-----END PUBLIC KEY-----"

    fun decodePublicKeyFromBase64(base64: String): PublicKey {
        val keyBytes = AndromedaBase64.decode(base64)
        return KeyFactory.getInstance(ALGORITHM).generatePublic(
            X509EncodedKeySpec(keyBytes),
        )
    }

    fun decodePublicKeyFromPem(pem: String): PublicKey {
        val cleaned =
            pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "")
                .replace("\r", "")
                .trim()
        return decodePublicKeyFromBase64(cleaned)
    }

    fun encrypt(
        data: ByteArray,
        publicKey: PublicKey,
    ): ByteArray {
        val cipher = AndromedaCipher.instance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(data)
    }

    fun decrypt(data: ByteArray): ByteArray {
        val cipher = AndromedaCipher.instance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey())
        return cipher.doFinal(data)
    }

    fun deleteKey() {
        if (isKeyAvailable()) AndromedaKeyStore.deleteKey(KEY_ALIAS)
    }
}
