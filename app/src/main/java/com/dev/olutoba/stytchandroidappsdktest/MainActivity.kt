package com.dev.olutoba.stytchandroidappsdktest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dev.olutoba.stytchandroidappsdktest.ui.screens.HeadlessScreen
import com.dev.olutoba.stytchandroidappsdktest.ui.screens.MainScreen
import com.dev.olutoba.stytchandroidappsdktest.ui.theme.StytchAndroidAppSDKTestTheme
import com.stytch.sdk.common.DeeplinkHandledStatus
import com.stytch.sdk.common.DeeplinkResponse
import com.stytch.sdk.consumer.StytchClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StytchAndroidAppSDKTestTheme {
                Surface(modifier = Modifier) {
                    var showHeadless by remember { mutableStateOf(false) }

                    if (showHeadless) {
                        HeadlessScreen(onBack = { showHeadless = false })
                    } else {
                        MainScreen(onLaunchHeadless = { showHeadless = true })
                    }
                }
            }
        }

        intent?.data?.let { handleDeepLink(it) }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { uri ->
            handleDeepLink(uri)
        }
    }
}

private fun handleDeepLink(uri: Uri) {
    // 60 = desired session duration in minutes (change if you need)
    StytchClient.handle(uri, 30) { deeplinkStatus ->
        when (deeplinkStatus) {
            is DeeplinkHandledStatus.Handled -> {
                when (val resp = deeplinkStatus.response) {
                    is DeeplinkResponse.Auth -> Log.d("DeepLinkAuthResponseLog", "$resp")
                    else -> Log.d("DeepLinkUnexpectedResponseTypeLog", "$resp")
                }
            }

            is DeeplinkHandledStatus.ManualHandlingRequired -> Log.d(
                "DeeplinkManualHandlingTTokenLog",
                deeplinkStatus.token
            )

            is DeeplinkHandledStatus.NotHandled -> Log.e(
                "DeepLinkNotHandledLog",
                deeplinkStatus.reason.message
            )
        }
    }
}
