package com.example.assignment_3.Data

import com.example.assignment_3.R

data class Hotel(val name: String, val imageRes: Int)

val hotelList =
        listOf(
                Hotel("Fairfield Hotel, Agra", R.drawable.fairfield_hotel),
                Hotel("Taj Hotel, Agra", R.drawable.taj_hotel),
                Hotel("Oberoi Hotel, Agra", R.drawable.oberoi_hotel)
        )
