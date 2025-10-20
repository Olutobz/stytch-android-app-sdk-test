package com.dev.olutoba.stytchandroidappsdktest.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.olutoba.stytchandroidappsdktest.viewmodels.HeadlessViewModel
import kotlinx.coroutines.launch

@Composable
fun HeadlessScreen(
    onBack: () -> Unit,
    viewModel: HeadlessViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Stytch Headless OTP Test", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Enter phone number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(), onClick = {
                if (phoneNumber.isBlank()) {
                    Toast.makeText(context, "Please enter valid number", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                scope.launch {
                    message = viewModel.sendOtp(phoneNumber)
                }
            }) {
            Text("Send OTP")
        }

        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("Enter OTP code") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(), onClick = {
                if (otp.isBlank()) {
                    Toast.makeText(context, "Enter valid OTP", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                scope.launch {
                    message = viewModel.verifyOtp(otp)
                }
            }) {
            Text("Verify OTP")
        }

        Spacer(Modifier.height(20.dp))
        Text(text = message)
        Spacer(Modifier.height(24.dp))

        TextButton(onClick = onBack) {
            Text("Go Home")
        }
    }
}
