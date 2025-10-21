package com.dev.olutoba.stytchandroidappsdktest.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.olutoba.stytchandroidappsdktest.models.AuthState
import com.stytch.sdk.common.DeeplinkHandledStatus
import com.stytch.sdk.common.DeeplinkResponse
import com.stytch.sdk.consumer.StytchClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * Created by Onikoyi Damola Olutoba
 * DATE: 21 October, 2025
 * EMAIL: damexxey94@gmail.com
 */


class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    fun handleDeeplink(uri: Uri) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            StytchClient.handle(uri = uri, sessionDurationMinutes = 60) {
                when (it) {
                    is DeeplinkHandledStatus.Handled -> {
                        when (val resp = it.response) {
                            is DeeplinkResponse.Auth -> {
                                Log.d("DeepLink", "AuthResponse: $resp")
                                _authState.value = AuthState.Authenticated
                            }

                            else -> {
                                Log.d("DeepLink", "Unexpected response: $resp")
                                _authState.value = AuthState.Error("Unexpected response")
                            }
                        }
                    }

                    is DeeplinkHandledStatus.ManualHandlingRequired -> {
                        Log.d("DeepLink", "Manual handling required, token=${it.token}")
                        _authState.value = AuthState.Error("Manual handling required")
                    }

                    is DeeplinkHandledStatus.NotHandled -> {
                        Log.e("DeepLink", "Not handled: ${it.reason.message}")
                        _authState.value =
                            AuthState.Error("Not handled: ${it.reason.message}")
                    }
                }
            }
        }
    }
}
