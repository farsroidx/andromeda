@file:Suppress("unused")

package andromeda.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaKeyStore {

    private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }

    fun isKeyAvailable(alias: String): Boolean = keyStore.containsAlias(alias)

    fun getAliases(): List<String> = keyStore.aliases().toList()

    fun initAesSecretKey(
        size: Int,
        alias: String,
        padding: String,
        algorithm: String,
        blockMode: String,
        authRequired: Boolean = false,
    ): SecretKey {
        return getSecretKey(alias = alias) ?: run {
            val keyGenerator = KeyGenerator.getInstance(algorithm, ANDROID_KEY_STORE)
            val purposes = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            val parameterSpec = KeyGenParameterSpec.Builder(alias, purposes)
                .setUserAuthenticationRequired(authRequired)
                .setRandomizedEncryptionRequired(true)
                .setEncryptionPaddings(padding)
                .setBlockModes(blockMode)
                .setKeySize(size)
                .build()
            keyGenerator.init(parameterSpec)
            return keyGenerator.generateKey()
        }
    }

    fun initKeyRsaPairs(
        size: Int,
        alias: String,
        padding: String,
        algorithm: String,
        blockMode: String,
        authRequired: Boolean = false,
    ) {
        if (!isKeyAvailable(alias)) {
            val keyPairGenerator = KeyPairGenerator.getInstance(algorithm, ANDROID_KEY_STORE)
            val purposes = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            val parameterSpec = KeyGenParameterSpec.Builder(alias, purposes)
                .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PSS)
                .setUserAuthenticationRequired(authRequired)
                .setEncryptionPaddings(padding)
                .setBlockModes(blockMode)
                .setKeySize(size)
                .setDigests(
                    KeyProperties.DIGEST_SHA512,
                    KeyProperties.DIGEST_SHA384,
                    KeyProperties.DIGEST_SHA256,
                    KeyProperties.DIGEST_SHA1
                )
                .build()
            keyPairGenerator.initialize(parameterSpec)
            keyPairGenerator.generateKeyPair()
        }
    }

    fun getSecretKey(alias: String): SecretKey? =
        (keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry)?.secretKey

    fun getPublicKey(alias: String): PublicKey? =
        (keyStore.getEntry(alias, null) as? KeyStore.PrivateKeyEntry)
            ?.certificate
            ?.publicKey

    fun getPrivateKey(alias: String): PrivateKey? =
        (keyStore.getEntry(alias, null) as? KeyStore.PrivateKeyEntry)
            ?.privateKey

    fun deleteKey(alias: String) {
        if (isKeyAvailable(alias)) keyStore.deleteEntry(alias)
    }

    fun deleteKeys() { keyStore.aliases().toList().forEach { keyStore.deleteEntry(it) } }
}
