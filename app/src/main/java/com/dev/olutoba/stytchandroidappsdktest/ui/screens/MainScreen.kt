package com.dev.olutoba.stytchandroidappsdktest.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.stytch.sdk.common.StytchResult
import com.stytch.sdk.ui.b2c.StytchUI
import com.stytch.sdk.ui.b2c.data.OAuthOptions
import com.stytch.sdk.ui.b2c.data.OAuthProvider
import com.stytch.sdk.ui.b2c.data.OTPMethods
import com.stytch.sdk.ui.b2c.data.OTPOptions
import com.stytch.sdk.ui.b2c.data.StytchProduct
import com.stytch.sdk.ui.b2c.data.StytchProductConfig

@Composable
fun MainScreen(onLaunchHeadless: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text("Stytch Android SDK Test", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { launchPrebuiltUI(activity) }) {
            Text("Launch Pre-Built UI")
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(onClick = onLaunchHeadless) {
            Text("Headless OTP Authentication")
        }
    }
}

private fun launchPrebuiltUI(activity: ComponentActivity?) {
    if (activity != null) {
        val stytchUI = StytchUI.Builder()
            .activity(activity)
            .productConfig(
                config = StytchProductConfig(
                    products = listOf(
                        StytchProduct.OAUTH,
                        StytchProduct.EMAIL_MAGIC_LINKS,
                        StytchProduct.OTP,
                        StytchProduct.PASSWORDS,
                    ),
                    oAuthOptions = OAuthOptions(
                        providers = listOf(
                            OAuthProvider.GOOGLE,
                            OAuthProvider.APPLE,
                            OAuthProvider.GITHUB
                        )
                    ),
                    otpOptions = OTPOptions(
                        methods = listOf(OTPMethods.SMS, OTPMethods.WHATSAPP),
                    )
                )
            )
            .onAuthenticated {
                when (it) {
                    is StytchResult.Success -> {
                        Log.d("Auth Success:", " ${it.value}")
                        Toast.makeText(
                            activity,
                            "Authentication Succeeded",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is StytchResult.Error -> {
                        Log.e("Auth Error:", it.exception.message)
                        Toast.makeText(
                            activity,
                            it.exception.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }.build()
        stytchUI.authenticate()
    }
}
