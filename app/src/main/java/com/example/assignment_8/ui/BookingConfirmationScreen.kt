package com.example.assignment_8.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment_8.Data.Room

@Composable
fun BookingConfirmationScreen(
    viewModel: HotelViewModel,
    onHomeClick: () -> Unit
) {
    val selectedHotel by viewModel.selectedHotel.collectAsState()
    val selectedRoom by viewModel.selectedRoom.collectAsState()
    val checkIn by viewModel.checkIn.collectAsState()
    val checkOut by viewModel.checkOut.collectAsState()
    val rooms by viewModel.rooms.collectAsState()
    val adults by viewModel.adults.collectAsState()
    val children by viewModel.children.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1F5FE))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(80.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Booking Confirmed!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF01579B)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Thank you, $userName. Your stay at ${selectedHotel?.name} has been successfully booked.",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("STAY DETAILS", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))
                
                DetailRow("Hotel", selectedHotel?.name ?: "N/A")
                DetailRow("Room Type", selectedRoom?.type ?: "N/A")
                DetailRow("Check-in", checkIn)
                DetailRow("Check-out", checkOut)
                DetailRow("Guests", "$adults Adults, $children Children")
                DetailRow("Rooms", rooms.toString())
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                
                Text("GUEST INFORMATION", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))
                DetailRow("Email", userEmail)
                DetailRow("Phone", userPhone)

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Price", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        "₹${(selectedRoom?.price ?: 0) * rooms}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF0277BD)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD))
        ) {
            Text("Go to Home", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1f).padding(start = 16.dp))
    }
}
