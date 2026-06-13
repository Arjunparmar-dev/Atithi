package com.example.assignment_8.Data

import com.example.assignment_3.R

data class Room(
    val name: String,
    val type: String,
    val price: Int,
    val hotelName: String,
    val imageRes: Int,
    val description: String
)

val roomList = listOf(
    Room(
        name = "Standard",
        type = "Standard Room",
        price = 3999,
        hotelName = "Fairfield Hotel, Agra",
        imageRes = R.drawable.fairfield_hotel,
        description = "Cozy room with basic amenities."
    ),
    Room(
        name = "Deluxe",
        type = "Deluxe Room",
        price = 5000,
        hotelName = "Fairfield Hotel, Agra",
        imageRes = R.drawable.fairfield_hotel,
        description = "Spacious room with modern decor."
    ),
    Room(
        name = "Suite",
        type = "Executive Suite",
        price = 9999,
        hotelName = "Fairfield Hotel, Agra",
        imageRes = R.drawable.fairfield_hotel,
        description = "Luxury suite with premium facilities."
    ),
    Room(
        name = "Premium",
        type = "Premium Room",
        price = 12000,
        hotelName = "Taj Hotel, Agra",
        imageRes = R.drawable.taj_hotel,
        description = "Elegant room with a touch of heritage."
    ),
    Room(
        name = "Royal Suite",
        type = "Royal Suite",
        price = 25000,
        hotelName = "Taj Hotel, Agra",
        imageRes = R.drawable.taj_hotel,
        description = "Stay like royalty in our finest suite."
    )
)
