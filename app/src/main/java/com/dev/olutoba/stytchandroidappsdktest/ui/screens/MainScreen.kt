package com.dev.olutoba.stytchandroidappsdktest.ui.screens

import android.util.Log
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
            .onAuthenticated {
                when (it) {
                    is StytchResult.Success -> Log.d("Auth Success:", " ${it.value}")
                    is StytchResult.Error -> Log.e("Auth Error:", "${it.exception}")
                }
            }
            .build()
        stytchUI.authenticate()
    }
}
