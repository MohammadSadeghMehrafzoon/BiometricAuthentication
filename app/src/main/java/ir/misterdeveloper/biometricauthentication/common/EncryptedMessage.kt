package ir.misterdeveloper.biometricauthentication.common

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

/**
 * Parcelable Data class with encrypted text, the initializationVector from the Cipher and timestamp
 */
@Parcelize
data class EncryptedMessage(
    val cipherText: ByteArray,
    val initializationVector: ByteArray,
    val savedAt: Long = System.currentTimeMillis()
): Parcelable
