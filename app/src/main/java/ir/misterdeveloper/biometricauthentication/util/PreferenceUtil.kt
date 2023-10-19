package ir.misterdeveloper.biometricauthentication.util

import android.content.Context
import com.google.gson.Gson
import ir.misterdeveloper.biometricauthentication.common.EncryptedMessage

/**
 * Helper class to save and retrieve EncryptedMessage
 */
object PreferenceUtil {

    private const val SHARED_PREFS_FILENAME = "biometric_prefs"

    /**
     * Saved and EncryptedMessage
     */
    fun storeEncryptedMessage(
        context: Context,
        prefKey: String,
        encryptedMessage: EncryptedMessage
    ) {
        val json = Gson().toJson(encryptedMessage)
        context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)
            .edit()
            .putString(prefKey, json).apply()
    }

    /**
     * Returns a single EncryptedMessage from prefKey
     */
    fun getEncryptedMessage(
        context: Context,
        prefKey: String
    ): EncryptedMessage? {
        val json = context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)
            .getString(prefKey, null)
        return Gson().fromJson(json, EncryptedMessage::class.java)
    }

    /**
     * Returns the list of all EncryptedMessage, the latest on top
     */
    fun getMessageList(context: Context): List<EncryptedMessage> {
        return context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE)
            .all
            .map { Gson().fromJson(it.value as String, EncryptedMessage::class.java) }
            .sortedBy { it.savedAt }
            .reversed()
    }

}

