package ir.misterdeveloper.biometricauthentication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.biometric.BiometricPrompt
import ir.misterdeveloper.biometricauthentication.MainActivity
import ir.misterdeveloper.biometricauthentication.R
import ir.misterdeveloper.biometricauthentication.common.BiometricAuthListener
import ir.misterdeveloper.biometricauthentication.common.BiometricUtil
import ir.misterdeveloper.biometricauthentication.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity(),
    BiometricAuthListener {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        showBiometricLoginOption()
    }

    fun onClickLogin(view: View) {
        navigateToListActivity()
    }

    fun onClickBiometrics(view: View) {
        BiometricUtil.showBiometricPrompt(
            activity = this,
            listener = this,
            cryptoObject = null,
            allowDeviceCredential = true
        )
    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        navigateToListActivity()
    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        Toast.makeText(this, "Biometric login failed. Error: $errorMessage", Toast.LENGTH_SHORT)
            .show()
    }

    private fun navigateToListActivity() {
        startActivity(Intent(this, ListActivity::class.java))
        finish()
    }

    private fun showBiometricLoginOption() {
        binding.buttonBiometricsLogin.visibility =
            if (BiometricUtil.isBiometricReady(this)) View.VISIBLE
            else View.GONE
    }

}
