package com.dev.olutoba.stytchandroidappsdktest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dev.olutoba.stytchandroidappsdktest.models.AuthState
import com.dev.olutoba.stytchandroidappsdktest.ui.screens.HeadlessScreen
import com.dev.olutoba.stytchandroidappsdktest.ui.screens.MainScreen
import com.dev.olutoba.stytchandroidappsdktest.ui.theme.StytchAndroidAppSDKTestTheme
import com.dev.olutoba.stytchandroidappsdktest.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StytchAndroidAppSDKTestTheme {
                Surface(modifier = Modifier) {
                    var showHeadless by remember { mutableStateOf(false) }
                    val authState by viewModel.authState.collectAsState()

                    if (showHeadless) {
                        HeadlessScreen(onBack = { showHeadless = false })
                    } else {
                        MainScreen(onLaunchHeadless = { showHeadless = true })
                    }

                    when (authState) {
                        is AuthState.Authenticated -> {
                            Log.d("DeepLinkSuccessLog", "Authenticated successfully!")
                        }

                        is AuthState.Error -> {
                            val errorMsg = (authState as AuthState.Error).message
                            Log.e("DeepLinkErrorLog", "Authentication failed: $errorMsg")
                        }

                        else -> {}
                    }
                }
            }
        }

        intent?.data?.let { handleDeepLink(it) }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { deepLinkUri ->
            handleDeepLink(deepLinkUri)
        }
    }

    private fun handleDeepLink(uri: Uri) {
        viewModel.handleDeeplink(uri)
    }
}

