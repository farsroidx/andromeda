@file:Suppress("unused", "DEPRECATION", "UseKtx")

package andromeda.crypto

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object AndromedaTokenStorage {
    private const val FILE_NAME = "token_prefs"

    private const val TOKEN_KEY = "encrypted_token"

    private lateinit var encryptedPrefs: SharedPreferences

    fun init(context: Context) {
        val masterKey =
            MasterKey
                .Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        encryptedPrefs =
            EncryptedSharedPreferences.create(
                context,
                FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
    }

    fun set(token: String) {
        val encryptedToken = AndromedaTokenManager.encrypt(token)
        val base64Token = AndromedaBase64.encode(encryptedToken)
        encryptedPrefs.edit().putString(TOKEN_KEY, base64Token).apply()
    }

    fun get(): String? {
        val base64Token = encryptedPrefs.getString(TOKEN_KEY, null) ?: return null
        val encryptedToken = AndromedaBase64.decode(base64Token)
        return AndromedaTokenManager.decrypt(encryptedToken)
    }

    fun clear() {
        encryptedPrefs.edit().remove(TOKEN_KEY).apply()
    }
}
