package com.example.assignment_8.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assignment_8.Data.Hotel
import com.example.assignment_8.Data.hotelList
import com.example.assignment_8.Data.roomList

@Composable
fun HotelApp(viewModel: HotelViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val authState by viewModel.authState.collectAsState()

    val showBottomBar = currentDestination?.route in listOf("find_room", "hotel_list", "faq")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    val gradient = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF42A5F5), Color(0xFF26C6DA))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(gradient),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Home,
                            label = "Home",
                            isSelected = currentDestination?.hierarchy?.any { it.route == "find_room" } == true,
                            onClick = {
                                navController.navigate("find_room") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        BottomNavItem(
                            icon = Icons.Default.LocationOn,
                            label = "Where2Go",
                            isSelected = currentDestination?.hierarchy?.any { it.route == "hotel_list" } == true,
                            onClick = {
                                navController.navigate("hotel_list") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        BottomNavItem(
                            icon = Icons.Default.Info,
                            label = "FAQs",
                            isSelected = currentDestination?.hierarchy?.any { it.route == "faq" } == true,
                            onClick = {
                                navController.navigate("faq") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(onTimeout = {
                    val destination = if (authState is HotelViewModel.AuthState.Success) "find_room" else "welcome"
                    navController.navigate(destination) {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("welcome") {
                WelcomeScreen(onGetStartedClick = {
                    navController.navigate("send_otp")
                })
            }
            composable("send_otp") {
                SendOTPScreen(
                    viewModel = viewModel,
                    onSendOTPClick = { navController.navigate("verify_otp") }
                )
            }
            composable("verify_otp") {
                VerifyOTPScreen(
                    viewModel = viewModel,
                    onVerifyClick = { navController.navigate("find_room") },
                    onEditPhoneClick = { navController.popBackStack() }
                )
            }
            composable("find_room") {
                FindRoomScreen(
                    hotelViewModel = viewModel,
                    onSearchClick = { navController.navigate("hotel_list") },
                    onLogoutClick = {
                        viewModel.logout()
                        navController.navigate("welcome") {
                            popUpTo(0)
                        }
                    }
                )
            }
            composable("hotel_list") {
                HotelListScreen(onHotelClick = { hotel ->
                    viewModel.setSelectedHotel(hotel)
                    navController.navigate("room_selection")
                })
            }
            composable("room_selection") {
                RoomSelectionScreen(
                    viewModel = viewModel,
                    onRoomSelected = { navController.navigate("confirmation") },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("confirmation") {
                BookingConfirmationScreen(
                    viewModel = viewModel,
                    onHomeClick = {
                        navController.navigate("find_room") {
                            popUpTo("find_room") { inclusive = true }
                        }
                    }
                )
            }
            composable("faq") { FAQItem() }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun HotelListScreen(onHotelClick: (Hotel) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(hotelList) { hotel ->
            val hotelRooms = roomList.filter { it.hotelName == hotel.name }

            var statusText = ""
            var isSoldOut = false

            if (hotelRooms.isEmpty()) {
                statusText = "Sold Out"
                isSoldOut = true
            } else {
                val prices = hotelRooms.map { it.price }
                val minPrice = prices.minOrNull() ?: 0
                val maxPrice = prices.maxOrNull() ?: 0
                statusText = "Rs.$minPrice to Rs.$maxPrice"
            }
            HotelItem(hotel = hotel, statusText = statusText, isSoldOut = isSoldOut, onClick = { onHotelClick(hotel) })
        }
    }
}

@Composable
fun HotelItem(hotel: Hotel, statusText: String, isSoldOut: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Image(
                painter = painterResource(hotel.imageRes),
                contentDescription = hotel.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .background(Color(0xFFE0E0E0))
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = hotel.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )

                val badgeColor = if (isSoldOut) Color.Red else Color(0xFF42A5F5)

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = badgeColor,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = statusText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
