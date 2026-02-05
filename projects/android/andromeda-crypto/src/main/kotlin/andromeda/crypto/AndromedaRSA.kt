@file:Suppress("unused")

package andromeda.crypto

import android.security.keystore.KeyProperties
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaRSA {

    private const val USER_AUTHENTICATION_REQUIRED = false // Set true if you want biometric prompt

    const val KEY_ALIAS = "ir.farsroidx.andromeda.rsa_key"
    const val ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA
    const val BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
    const val PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_OAEP
    const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    const val KEY_SIZE = 4096

    private var optimalSpec: OAEPParameterSpec? = null

    fun initKeyPair(identifier: String? = null) {
        if (!AndromedaKeyStore.isKeyAvailable(alias = getAliasKey(identifier))) {
            AndromedaKeyStore.initKeyRsaPairs(
                size = KEY_SIZE,
                alias = getAliasKey(identifier),
                padding = PADDING,
                algorithm = ALGORITHM,
                blockMode = BLOCK_MODE,
                authRequired = USER_AUTHENTICATION_REQUIRED,
            )
        }
    }

    private fun getPrivateKey(identifier: String? = null): PrivateKey {
        initKeyPair(identifier)
        return AndromedaKeyStore.getPrivateKey(alias = getAliasKey(identifier))!!
    }

    fun getPublicKey(identifier: String? = null): PublicKey {
        initKeyPair(identifier)
        return AndromedaKeyStore.getPublicKey(alias = getAliasKey(identifier))!!
    }

    fun getPublicKeyBase64(identifier: String? = null): String =
        AndromedaBase64.encode(value = getPublicKey(identifier).encoded)

    fun getPublicKeyPem(identifier: String? = null): String = buildString {
        appendLine(value = "-----BEGIN PUBLIC KEY-----")
        appendLine(value = getPublicKeyBase64(identifier).chunked(64).joinToString("\n"))
        appendLine(value = "-----END PUBLIC KEY-----")
    }

    fun decodePublicKeyFromBase64(base64: String): PublicKey {
        val keyBytes = AndromedaBase64.decode(base64)
        return KeyFactory.getInstance(ALGORITHM).generatePublic(
            X509EncodedKeySpec(keyBytes),
        )
    }

    fun decodePublicKeyFromPem(pem: String): PublicKey {
        val cleaned = pem
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\n", "")
            .replace("\r", "")
            .trim()
        return decodePublicKeyFromBase64(cleaned)
    }

    fun encrypt(data: ByteArray, identifier: String? = null): ByteArray {

        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getPublicKey(identifier),
            getOAEPParameterSpec(identifier)
        )

        return cipher.doFinal(data)
    }

    fun decrypt(data: ByteArray, identifier: String? = null): ByteArray {

        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.DECRYPT_MODE,
            getPrivateKey(identifier),
            getOAEPParameterSpec(identifier)
        )

        return cipher.doFinal(data)
    }

    fun deleteKey(identifier: String? = null) =
        AndromedaKeyStore.deleteKey(alias = getAliasKey(identifier))

    fun deleteKeys() {

        AndromedaKeyStore.getAliases().forEach {

            if (it.startsWith(prefix = AndromedaAES.KEY_ALIAS)) {
                AndromedaKeyStore.deleteKey(alias = it)
            }
        }
    }

    private fun getAliasKey(identifier: String? = null): String {
        return if (identifier == null) KEY_ALIAS else "$KEY_ALIAS.$identifier"
    }

    private fun getOAEPParameterSpec(identifier: String? = null): OAEPParameterSpec {

        optimalSpec?.let { return it }

        val testData = "test".toByteArray()
        val pubKey = getPublicKey(identifier)
        val priKey = getPrivateKey(identifier)

        val options = listOf(
            MGF1ParameterSpec.SHA512,
            MGF1ParameterSpec.SHA384,
            MGF1ParameterSpec.SHA256,
            MGF1ParameterSpec.SHA1
        )

        for (mgf1Digest in options) {

            try {

                val spec = OAEPParameterSpec(
                    "SHA-1",
                    "MGF1",
                    mgf1Digest,
                    PSource.PSpecified.DEFAULT
                )

                val encCipher = Cipher.getInstance(TRANSFORMATION)
                encCipher.init(Cipher.ENCRYPT_MODE, pubKey, spec)
                val encrypted = encCipher.doFinal(testData)

                val decCipher = Cipher.getInstance(TRANSFORMATION)
                decCipher.init(Cipher.DECRYPT_MODE, priKey, spec)
                decCipher.doFinal(encrypted)

                optimalSpec = spec

                return spec

            } catch (e: Exception) {
                continue
            }
        }

        val fallback = OAEPParameterSpec(
            "SHA-1",
            "MGF1",
            MGF1ParameterSpec.SHA1,
            PSource.PSpecified.DEFAULT
        )

        optimalSpec = fallback

        return fallback
    }
}