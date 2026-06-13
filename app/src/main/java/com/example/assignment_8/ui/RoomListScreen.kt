package com.example.assignment_8.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment_8.Data.Room
import com.example.assignment_8.Data.roomList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(viewModel: HotelViewModel, onBackClick: () -> Unit) {
    val selectedHotel by viewModel.selectedHotel.collectAsState()
    val rooms = roomList.filter { it.hotelName == selectedHotel?.name }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedHotel?.name ?: "Rooms") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF42A5F5),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (rooms.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No rooms available for this hotel.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(rooms) { room ->
                    RoomItem(room)
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: Room) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = room.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Price: Rs. ${room.price}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            Button(
                onClick = { /* Handle Booking */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006064))
            ) {
                Text("Book Now")
            }
        }
    }
}
