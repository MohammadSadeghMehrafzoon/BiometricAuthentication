package ir.misterdeveloper.biometricauthentication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.misterdeveloper.biometricauthentication.R

import android.content.DialogInterface
import android.os.Build
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import ir.misterdeveloper.biometricauthentication.common.BiometricAuthListener
import ir.misterdeveloper.biometricauthentication.common.BiometricUtil
import ir.misterdeveloper.biometricauthentication.common.CommonUtils
import ir.misterdeveloper.biometricauthentication.databinding.ActivityEncryptionBinding
import ir.misterdeveloper.biometricauthentication.util.CryptographyUtil
import ir.misterdeveloper.biometricauthentication.util.PreferenceUtil
import javax.crypto.Cipher

class EncryptionActivity : AppCompatActivity(),
    BiometricAuthListener {

    lateinit var  binding: ActivityEncryptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEncryptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onClickEncryptMessage(view: View) {
        val message = binding.textInputMessage.editText?.text.toString().trim()
        if (!TextUtils.isEmpty(message)) {
            if (BiometricUtil.isBiometricReady(this)) {
                showBiometricPromptToEncrypt()
            } else {
                showAlertToSetupBiometric()
            }
        }
    }

    private fun confirmInput() {
        binding.textInputMessage.editText?.text = null
        CommonUtils.displayToast(this, getString(R.string.message_saved))
    }

    private fun showAlertToSetupBiometric() {
        CommonUtils.displayMessage(
            this,
            getString(R.string.message_encryption_failed),
            getString(R.string.message_no_biometric),
        ) { dialog: DialogInterface, index: Int ->
            BiometricUtil.lunchBiometricSettings(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showBiometricPromptToEncrypt() {
        // Create Cryptography Object
        val cryptoObject = BiometricPrompt.CryptoObject(
            CryptographyUtil.getInitializedCipherForEncryption()
        )
        // Show BiometricPrompt
        BiometricUtil.showBiometricPrompt(
            activity = this,
            listener = this,
            cryptoObject = cryptoObject
        )
    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        result.cryptoObject?.cipher?.let {
            val message = binding.textInputMessage.editText?.text.toString().trim()
            if (!TextUtils.isEmpty(message)) {
                encryptAndSave(message, it)
                confirmInput()
            }
        }
    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        CommonUtils.displayToast(this, "Biometric error: $errorMessage")
    }

    private fun encryptAndSave(plainTextMessage: String, cipher: Cipher) {
        val encryptedMessage = CryptographyUtil.encryptData(plainTextMessage, cipher)
        // Save Encrypted Message
        PreferenceUtil.storeEncryptedMessage(
            applicationContext,
            prefKey = encryptedMessage.savedAt.toString(),
            encryptedMessage = encryptedMessage
        )
    }

}
