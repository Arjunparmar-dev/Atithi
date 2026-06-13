package com.example.assignment_8.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.example.assignment_8.Data.Place
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindRoomScreen(hotelViewModel: HotelViewModel, onSearchClick: () -> Unit, onLogoutClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLocation by hotelViewModel.location.collectAsState()
    val places by hotelViewModel.places.collectAsState()
    
    // Dynamically update options from places list or use fallback
    val options = remember(places) {
        if (places.isNotEmpty()) {
            places.map { it.name }
        } else {
            listOf("Agra", "Bengaluru", "Chennai")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Find Room",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onLogoutClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Logout",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Logout", fontSize = 16.sp)
            }
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedLocation,
                onValueChange = { },
                label = { Text("Where you want to go?") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, expanded)
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                readOnly = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Where?",
                        tint = Color(0xFF42A5F5)
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            hotelViewModel.setLocation(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        val checkIn by hotelViewModel.checkIn.collectAsState()
        val checkOut by hotelViewModel.checkOut.collectAsState()
        val rooms by hotelViewModel.rooms.collectAsState()
        val adults by hotelViewModel.adults.collectAsState()
        val children by hotelViewModel.children.collectAsState()

        MyDatePicker("Check-in Date", checkIn) { hotelViewModel.setCheckIn(it) }
        MyDatePicker("Check-out Date", checkOut) { hotelViewModel.setCheckOut(it) }

        TextField(
            value = "$rooms Room, $adults Adult, $children Child",
            onValueChange = {},
            label = { Text("Guests & Rooms") },
            enabled = false,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .clickable { hotelViewModel.setBedFlag(true) },
            colors = TextFieldDefaults.colors(
                disabledLabelColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = Color.White,
                disabledIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledPlaceholderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "guests",
                    tint = Color(0xFF42A5F5)
                )
            }
        )

        val gradient = Brush.horizontalGradient(
            colors = listOf(Color(0xFF42A5F5), Color(0xFF26C6DA))
        )

        Button(
            onClick = onSearchClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(gradient, shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            enabled = selectedLocation.isNotEmpty() && checkIn.isNotEmpty() && checkOut.isNotEmpty()
        ) {
            Text(text = "SEARCH", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BEST PLACES",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 16.sp
            )
            Text(
                text = "VIEW ALL",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF42A5F5),
                fontSize = 16.sp,
                modifier = Modifier.clickable {  }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(places) { place ->
                PlaceCard(place)
            }
        }

        val bedFlag by hotelViewModel.bedFlag.collectAsState()
        if (bedFlag) {
            GuestSelectorDialog(
                onDismissRequest = { hotelViewModel.setBedFlag(false) },
                hotelViewModel = hotelViewModel
            )
        }
    }
}

@Composable
fun PlaceCard(place: Place) {
    Card(
        modifier = Modifier.width(160.dp).height(180.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
    ) {
        Column {
            AsyncImage(
                model = place.image,
                contentDescription = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = place.name,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    label: String,
    check: String,
    dateValue: (String) -> Unit,
) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            dateValue("$dayOfMonth/${month + 1}/$year")
        }, mYear, mMonth, mDay
    )

    TextField(
        value = check,
        onValueChange = { dateValue(it) },
        label = { Text(label) },
        enabled = false,
        modifier = Modifier
            .clickable { mDatePickerDialog.show() }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = TextFieldDefaults.colors(
            disabledLabelColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = Color.White,
            disabledIndicatorColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "date",
                tint = Color(0xFF42A5F5)
            )
        }
    )
}

@Composable
fun GuestSelectorDialog(
    onDismissRequest: () -> Unit,
    hotelViewModel: HotelViewModel
) {
    val rooms by hotelViewModel.rooms.collectAsState()
    val adults by hotelViewModel.adults.collectAsState()
    val children by hotelViewModel.children.collectAsState()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Guests & Rooms",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CounterRow("Rooms", rooms) { hotelViewModel.setRooms(it) }
                CounterRow("Adults", adults) { hotelViewModel.setAdults(it) }
                CounterRow("Children", children) { hotelViewModel.setChildren(it) }

                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5))
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun CounterRow(label: String, count: Int, onCountChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 18.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (count > 0) onCountChange(count - 1) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Decrease")
            }
            Text(text = count.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onCountChange(count + 1) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Increase")
            }
        }
    }
}
