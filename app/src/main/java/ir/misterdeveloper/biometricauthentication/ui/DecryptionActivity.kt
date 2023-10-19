package ir.misterdeveloper.biometricauthentication.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import ir.misterdeveloper.biometricauthentication.R
import ir.misterdeveloper.biometricauthentication.common.BiometricAuthListener
import ir.misterdeveloper.biometricauthentication.common.BiometricUtil
import ir.misterdeveloper.biometricauthentication.common.CommonUtils
import ir.misterdeveloper.biometricauthentication.common.EncryptedMessage
import ir.misterdeveloper.biometricauthentication.databinding.ActivityDecryptionBinding
import ir.misterdeveloper.biometricauthentication.util.CryptographyUtil
import javax.crypto.Cipher

class DecryptionActivity : AppCompatActivity(),
    BiometricAuthListener {

    var encryptedMessage: EncryptedMessage? = null
    lateinit var binding: ActivityDecryptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDecryptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        encryptedMessage = intent.getParcelableExtra(getString(R.string.parcel_message))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onClickDecryptMessage(view: View) {
        showBiometricPromptToDecrypt()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showBiometricPromptToDecrypt() {
        encryptedMessage?.initializationVector?.let { it ->
            // Retrieve Cryptography Object
            val cryptoObject = BiometricPrompt.CryptoObject(
                CryptographyUtil.getInitializedCipherForDecryption(it)
            )
            // Show BiometricPrompt With Cryptography Object
            BiometricUtil.showBiometricPrompt(
                activity = this,
                listener = this,
                cryptoObject = cryptoObject
            )
        }
    }

    private fun decryptAndDisplay(cipher: Cipher) {
        encryptedMessage?.cipherText?.let { it ->
            val decryptedMessage = CryptographyUtil.decryptData(it, cipher)
            binding.textViewMessage.text = decryptedMessage
        }
    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        result.cryptoObject?.cipher?.let {
            decryptAndDisplay(it)
        }
    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        CommonUtils.displayToast(this, "Biometric error: $errorMessage")
    }

}
