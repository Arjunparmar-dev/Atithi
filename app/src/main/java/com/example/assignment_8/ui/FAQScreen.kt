package com.example.assignment_8.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment_3.R

@Composable
fun FAQItem(){
    Column(
        Modifier.verticalScroll(rememberScrollState())

    ) {
        Image(
            painter = painterResource(id = R.drawable.faq), contentDescription = "faq cover",
        )
        Text(
            text = "1.What happens if I enter  the wrong OTP during account verification?",
            Modifier.padding(horizontal = 15.dp),
            color = Color(0xFF338AE0),
            textAlign = androidx.compose.ui.text.style.TextAlign.Justify,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "If you enter the wrong OPT, you will be shown an error message and will have to re-enter the correct code. Please mention the correct OPT that you receive on your registred mobile number via SMS to proceed with the verification process successfully.",
            Modifier.padding(horizontal = 15.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Justify,
            fontSize = 20.sp
        )
        Text(
            text = "2.Where can I contact if I face any issues while booking a hotel room using the app?",
            Modifier.padding(horizontal = 15.dp),
            color = Color(0xFF338AE0),
            textAlign = androidx.compose.ui.text.style.TextAlign.Justify,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "If you face any issues during your booking process you can reach out to our customer support team by at dummy@book_nest.com or call us at 9999999999.",
            Modifier.padding(horizontal = 15.dp),
            fontSize = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Justify
        )
        Text(
            text = "3.Is my personal information secure when using the app?",
            Modifier.padding(horizontal = 15.dp),
            color = Color(0xFF338AE0),
            fontSize = 22.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Justify,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Yes, we value the trust of our users and prioritise the security of their personal information. All your dat, including personal details and booking information is secure with us and protected using encryption protocols to ensure confidentiality.",
            Modifier.padding(horizontal = 15.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Justify,
            fontSize = 20.sp,
        )
    }
}