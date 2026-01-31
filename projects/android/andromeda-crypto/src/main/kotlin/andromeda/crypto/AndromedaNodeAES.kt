@file:Suppress("unused", "SpellCheckingInspection")

package andromeda.crypto

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// ---------------- DataStore -----------------
private val Context.dataStore by preferencesDataStore(name = "aes_prefs")

/**
 * [AndromedaNodeAES] is a secure wrapper for managing a server-provided AES key and encrypting/decrypting payloads.
 *
 * This object handles:
 * 1. Securely storing a server-provided AES key in [androidx.datastore.core.DataStore] encrypted with a local hardware-backed AES key (via [AndromedaAES]).
 * 2. Encrypting payloads using AES-GCM with a random IV for each encryption.
 * 3. Decrypting payloads using the stored AES key and validating authenticity with GCM's authentication tag.
 * 4. Deleting the stored AES key securely.
 *
 * ## Security Notes
 * - AES-GCM provides **authenticated encryption**, ensuring that tampered ciphertext will fail decryption.
 * - IV is generated randomly for each encryption and prepended to the ciphertext.
 * - The AES key is never exposed outside this object in raw form.
 * - Use coroutine context properly; all suspend functions should not be called on the main thread for large payloads.
 *
 * ## Usage Example
 * ```kotlin
 * // Store AES key from server
 * AndromedaServerAES.storeServerKey(context, serverKeyBytes)
 *
 * // Encrypt data
 * val encrypted = AndromedaServerAES.encrypt(context, "Hello".toByteArray())
 *
 * // Decrypt data
 * val decrypted = AndromedaServerAES.decrypt(context, encrypted)
 * println(String(decrypted)) // "Hello"
 *
 * // Delete AES key
 * AndromedaServerAES.deleteKey(context)
 * ```
 */
object AndromedaNodeAES {
    /**
     * Preference key used to store the encrypted server AES key
     * inside Jetpack DataStore.
     */
    private val AES_KEY_PREF = stringPreferencesKey("AES_SERVER_KEY")

    /**
     * AES transformation used for payload encryption/decryption.
     */
    private const val TRANSFORMATION = "AES/GCM/NoPadding"

    /**
     * Recommended IV size for AES-GCM (96 bits / 12 bytes).
     */
    private const val IV_SIZE = 12

    /**
     * Authentication tag size in bits for AES-GCM.
     */
    private const val TAG_SIZE = 128

    /**
     * Encrypts and stores a server-provided AES key securely using the local hardware-backed AES key.
     *
     * @param context Context used to access [androidx.datastore.core.DataStore].
     * @param serverAesKey The raw AES key bytes received from the server.
     */
    suspend fun storeServerKey(
        context: Context,
        serverAesKey: ByteArray,
    ) {
        val cipher = AndromedaCipher.instance(AndromedaAES.TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, AndromedaAES.getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(serverAesKey)
        val combined = iv + encrypted
        val encoded = AndromedaBase64.encode(combined)
        context.dataStore.edit { prefs -> prefs[AES_KEY_PREF] = encoded }
    }

    /**
     * Retrieves and decrypts the stored AES key.
     *
     * @param context Context used to access [androidx.datastore.core.DataStore].
     * @return [SecretKey] instance for AES encryption/decryption, or `null` if no key is stored.
     */
    private suspend fun getServerKey(context: Context): SecretKey? {
        val encoded = context.dataStore.data.first()[AES_KEY_PREF] ?: return null
        val combined = AndromedaBase64.decode(encoded)
        val iv = combined.copyOfRange(0, 16)
        val encrypted = combined.copyOfRange(16, combined.size)
        val cipher = AndromedaCipher.instance(AndromedaAES.TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, AndromedaAES.getSecretKey(), IvParameterSpec(iv))
        val keyBytes = cipher.doFinal(encrypted)
        return SecretKeySpec(keyBytes, AndromedaAES.ALGORITHM)
    }

    /**
     * Encrypts the given payload using the stored server AES key with AES-GCM.
     *
     * @param context Context used to access [androidx.datastore.core.DataStore].
     * @param data Raw payload bytes to encrypt.
     * @return Encrypted payload with the IV prepended.
     * @throws IllegalStateException if the AES key is not available.
     */
    suspend fun encrypt(
        context: Context,
        data: ByteArray,
    ): ByteArray {
        val aesKey = getServerKey(context) ?: throw IllegalStateException("AES key not available")
        val iv = ByteArray(IV_SIZE)
        java.security.SecureRandom().nextBytes(iv)
        val gcmSpec = GCMParameterSpec(TAG_SIZE, iv)
        val cipher = AndromedaCipher.instance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec)
        val encrypted = cipher.doFinal(data)
        return iv + encrypted
    }

    /**
     * Decrypts the given AES-GCM encrypted payload using the stored server AES key.
     *
     * @param context Context used to access [androidx.datastore.core.DataStore].
     * @param data Encrypted payload with prepended IV.
     * @return Decrypted payload bytes.
     * @throws IllegalStateException if the AES key is not available.
     * @throws IllegalArgumentException if the encrypted data is invalid or tampered.
     */
    suspend fun decrypt(
        context: Context,
        data: ByteArray,
    ): ByteArray {
        val aesKey = getServerKey(context) ?: throw IllegalStateException("AES key not available")
        if (data.size <= IV_SIZE) throw IllegalArgumentException("Invalid encrypted data")
        val iv = data.copyOfRange(0, IV_SIZE)
        val encryptedData = data.copyOfRange(IV_SIZE, data.size)
        val gcmSpec = GCMParameterSpec(TAG_SIZE, iv)
        val cipher = AndromedaCipher.instance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmSpec)
        return cipher.doFinal(encryptedData)
    }

    /**
     * Deletes the stored server AES key from secure storage.
     *
     * @param context Context used to access [androidx.datastore.core.DataStore].
     */
    suspend fun deleteKey(context: Context) {
        context.dataStore.edit { it.remove(AES_KEY_PREF) }
    }
}
