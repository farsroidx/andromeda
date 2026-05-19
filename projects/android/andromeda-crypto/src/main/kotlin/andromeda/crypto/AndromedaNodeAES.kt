@file:Suppress("unused")

package andromeda.crypto

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

private val Context.dataStore by preferencesDataStore(name = "aes_prefs")

/**
 * Manages the storage and usage of the Server's AES key on the Android client.
 * This object handles secondary encryption of the server key before storing it in DataStore.
 */
object AndromedaNodeAES {
    /**
     * Encrypts and persists the decrypted AES key received from the server.
     *
     * @param context Android context for DataStore access.
     * @param aesKey The raw (already decrypted via RSA) AES key bytes.
     * @param identifier Optional unique identifier for the account/session.
     */
    suspend fun setAesKey(
        context: Context,
        aesKey: ByteArray,
        identifier: String? = null,
    ) {
        val cipher = Cipher.getInstance(AndromedaAES.TRANSFORMATION)

        // We use the Local AndroidKeyStore AES key to encrypt the Server's AES key
        cipher.init(Cipher.ENCRYPT_MODE, AndromedaAES.getSecretKey(identifier))

        val iv = cipher.iv // Typically 12 bytes for GCM
        val encrypted = cipher.doFinal(aesKey)
        val combined = iv + encrypted

        val encoded = AndromedaBase64.encode(combined)

        context.dataStore.edit { prefs -> prefs[getPrefKey(identifier)] = encoded }
    }

    /**
     * Retrieves and decrypts the Server's AES key from DataStore.
     *
     * @param context Android context for DataStore access.
     * @param identifier Optional unique identifier for the account/session.
     * @return The decrypted [SecretKey] ready for data encryption/decryption.
     */
    private suspend fun getServerKey(
        context: Context,
        identifier: String? = null,
    ): SecretKey? {
        val encoded = context.dataStore.data.first()[getPrefKey(identifier)] ?: return null

        val combined = AndromedaBase64.decode(encoded)

        // CORRECTION: Use AndromedaAES.IV_SIZE (12) instead of 16
        val ivSize = AndromedaAES.IV_SIZE
        if (combined.size <= ivSize) return null

        val iv = combined.copyOfRange(0, ivSize)
        val encrypted = combined.copyOfRange(ivSize, combined.size)

        val cipher = Cipher.getInstance(AndromedaAES.TRANSFORMATION)
        // CORRECTION: Use GCMParameterSpec for GCM mode, not IvParameterSpec
        val spec = GCMParameterSpec(AndromedaAES.TAG_SIZE, iv)

        cipher.init(Cipher.DECRYPT_MODE, AndromedaAES.getSecretKey(identifier), spec)

        val keyBytes = cipher.doFinal(encrypted)

        return SecretKeySpec(keyBytes, AndromedaAES.ALGORITHM)
    }

    suspend fun encrypt(
        context: Context,
        data: ByteArray,
        identifier: String? = null,
    ): ByteArray {
        val aesKey =
            getServerKey(context, identifier)
                ?: throw IllegalStateException("Server AES key not found. Perform key exchange first.")

        val iv = ByteArray(AndromedaAES.IV_SIZE).also { SecureRandom().nextBytes(it) }
        val gcmSpec = GCMParameterSpec(AndromedaAES.TAG_SIZE, iv)
        val cipher = Cipher.getInstance(AndromedaAES.TRANSFORMATION)

        cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec)

        val encrypted = cipher.doFinal(data)

        return iv + encrypted
    }

    suspend fun decrypt(
        context: Context,
        data: ByteArray,
        identifier: String? = null,
    ): ByteArray {
        val aesKey =
            getServerKey(context, identifier)
                ?: throw IllegalStateException("Server AES key not found.")

        if (data.size <= AndromedaAES.IV_SIZE) {
            throw IllegalArgumentException("Payload too short to contain IV")
        }

        val iv = data.copyOfRange(0, AndromedaAES.IV_SIZE)
        val encryptedData = data.copyOfRange(AndromedaAES.IV_SIZE, data.size)

        val gcmSpec = GCMParameterSpec(AndromedaAES.TAG_SIZE, iv)
        val cipher = Cipher.getInstance(AndromedaAES.TRANSFORMATION)

        cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmSpec)
        return cipher.doFinal(encryptedData)
    }

    private fun getPrefKey(identifier: String? = null): Preferences.Key<String> =
        stringPreferencesKey(
            name = if (identifier == null) "SERVER_AES_KEY" else "SERVER_AES_KEY.$identifier",
        )
}
