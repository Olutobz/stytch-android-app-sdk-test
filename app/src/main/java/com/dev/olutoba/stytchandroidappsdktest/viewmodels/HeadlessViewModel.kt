package com.dev.olutoba.stytchandroidappsdktest.viewmodels

import androidx.lifecycle.ViewModel
import com.stytch.sdk.common.StytchResult
import com.stytch.sdk.consumer.StytchClient
import com.stytch.sdk.consumer.otp.OTP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeadlessViewModel : ViewModel() {
    private var methodId: String? = null

    suspend fun sendOtp(phoneNumber: String): String {
        return withContext(Dispatchers.IO) {
            val otpResponse = StytchClient.otps.sms.loginOrCreate(
                parameters = OTP.SmsOTP.Parameters(phoneNumber = phoneNumber)
            )
            when (otpResponse) {
                is StytchResult.Success -> {
                    methodId = otpResponse.value.methodId
                    "OTP sent successfully"
                }

                is StytchResult.Error -> {
                    "Failed to send OTP: ${otpResponse.exception.message}"
                }
            }
        }
    }

    suspend fun verifyOtp(code: String): String {
        return withContext(Dispatchers.IO) {
            val method =
                methodId ?: return@withContext "No methodId found! Please request OTP first."
            val response = StytchClient.otps.authenticate(
                parameters = OTP.AuthParameters(token = code, methodId = method)
            )
            when (response) {
                is StytchResult.Success -> "Authenticated successfully"
                is StytchResult.Error -> "Authentication failed: ${response.exception.message}"
            }
        }
    }
}
