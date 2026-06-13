package com.example.assignment_8.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment_8.ui.HotelViewModel

@Composable
fun VerifyOTPScreen(
    viewModel: HotelViewModel,
    onVerifyClick: () -> Unit,
    onEditPhoneClick: () -> Unit
) {
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val authState by viewModel.authState.collectAsState()
    var otpCode by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is HotelViewModel.AuthState.Success) {
            onVerifyClick()
        } else if (authState is HotelViewModel.AuthState.Error) {
            Toast.makeText(context, (authState as HotelViewModel.AuthState.Error).message, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF48A5EF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp).align(Alignment.Start)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "VERIFY MOBILE NUMBER",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "OTP has been sent to you on your mobile number, please enter it below",
                    fontSize = 18.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(vertical = 8.dp)
                        .fillMaxWidth(),
                )

                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { if (it.length <= 6) otpCode = it },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    label = { Text("Enter 6-digit OTP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("") }
                )

                if (authState is HotelViewModel.AuthState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
                }

                Button(
                    onClick = {
                        if (otpCode.length == 6) {
                            viewModel.verifyOtp(otpCode)
                        } else {
                            Toast.makeText(context, "Enter 6 digit OTP", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF74AFE0)),
                    enabled = authState !is HotelViewModel.AuthState.Loading
                ) {
                    Text(text = "Verify OTP", color = Color.Black , fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { if (!isTimerRunning) viewModel.sendOtp(context as Activity) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD)),
                    enabled = !isTimerRunning && authState !is HotelViewModel.AuthState.Loading
                ) {
                    val timerText = if (timeLeft > 0) {
                        val seconds = timeLeft.toString().padStart(2, '0')
                        "Resend OTP (00:$seconds)"
                    } else {
                        "Resend OTP"
                    }
                    Text(text = timerText, color = Color.White)
                }

                Button(
                    onClick = onEditPhoneClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD))
                ) {
                    Text(text = "Edit Phone Number", color = Color.White)
                }
            }
        }
    }
}
