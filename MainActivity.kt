package com.hadidental.clinic

import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Navy = Color(0xFF064B7D)
private val Blue = Color(0xFF0797D0)
private val Pale = Color(0xFFEAF6FC)
private val Ink = Color(0xFF153B5B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HadiDentalApp(
            onCall = { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:7006637905"))) },
            onMap = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Ali+Larijani+Chowk,+Sonapah,+Beerwah,+Jammu+and+Kashmir"))) }
        ) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadiDentalApp(onCall: () -> Unit, onMap: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    Scaffold(
        containerColor = Color(0xFFF7FBFE),
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
                NavigationBarItem(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.CalendarMonth, null) }, label = { Text("Book") })
                NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.MedicalServices, null) }, label = { Text("Services") })
                NavigationBarItem(selected = selectedTab == 3, onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.LocationOn, null) }, label = { Text("Contact") })
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> HomeScreen(padding, onCall, onMap, onBook = { selectedTab = 1 })
            1 -> BookingScreen(padding)
            2 -> ServicesScreen(padding)
            else -> ContactScreen(padding, onCall, onMap)
        }
    }
}

@Composable
fun HomeScreen(padding: PaddingValues, onCall: () -> Unit, onMap: () -> Unit, onBook: () -> Unit) {
    Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(Modifier.fillMaxWidth().background(Navy).padding(22.dp)) {
            Column {
                Text("HADI", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                Text("DENTAL CLINIC & PROSTHESIS", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Healthy Smiles, Brighter Futures", color = Color(0xFFBFEAFF), fontSize = 15.sp)
            }
        }
        Column(Modifier.padding(18.dp)) {
            Text("Your smile deserves the best care!", color = Navy, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            Text("Professional dental care in a clean, comfortable and patient-friendly environment.",
                color = Ink, fontSize = 15.sp, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
            Button(onClick = onBook, modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.CalendarMonth, null); Spacer(Modifier.width(8.dp)); Text("Book an Appointment")
            }
            OutlinedButton(onClick = onCall, modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(50.dp),
                shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.Call, null); Spacer(Modifier.width(8.dp)); Text("Call 70006637905")
            }
            Spacer(Modifier.height(22.dp))
            Text("Our Dental Services", color = Navy, fontSize = 21.sp, fontWeight = FontWeight.Bold)
            ServiceGrid()
            Spacer(Modifier.height(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Pale), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Owned & Managed by", color = Ink, fontWeight = FontWeight.Medium)
                    Text("Barket Ali Malik", color = Navy, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("The dental hygienist", color = Ink)
                }
            }
            Spacer(Modifier.height(14.dp))
            Text("Ali Larijani Chowk, Sonapah, Beerwah – 193411", color = Ink)
            TextButton(onClick = onMap) { Icon(Icons.Default.Map, null); Text("Get directions") }
        }
    }
}

@Composable
fun ServiceGrid() {
    val services = listOf(
        "General Dentistry", "Orthodontic Treatment", "Dental Implants",
        "Prosthesis (Artificial Teeth)", "Teeth Cleaning & Whitening", "Emergency Dental Care"
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        services.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { name ->
                    Card(Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(1.dp)) {
                        Column(Modifier.padding(13.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Favorite, null, tint = Blue, modifier = Modifier.size(25.dp))
                            Spacer(Modifier.height(6.dp))
                            Text(name, color = Navy, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ServicesScreen(padding: PaddingValues) {
    Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp)) {
        Text("Our Services", color = Navy, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Dental care services listed by Hadi Dental Clinic & Prosthesis.", color = Ink, modifier = Modifier.padding(top = 6.dp, bottom = 16.dp))
        ServiceGrid()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(padding: PaddingValues) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("General Dentistry") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("Morning") }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val options = listOf("General Dentistry", "Orthodontic Treatment", "Dental Implants",
        "Prosthesis (Artificial Teeth)", "Teeth Cleaning & Whitening", "Emergency Dental Care")

    Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp)) {
        Text("Book an Appointment", color = Navy, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Send an appointment request to the clinic.", color = Ink, modifier = Modifier.padding(top = 5.dp, bottom = 16.dp))
        OutlinedTextField(name, { name = it }, label = { Text("Patient name *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(phone, { phone = it }, label = { Text("Mobile number *") }, modifier = Modifier.fillMaxWidth(),
            singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        Spacer(Modifier.height(10.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(service, {}, readOnly = true, label = { Text("Select service *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { item -> DropdownMenuItem(text = { Text(item) }, onClick = { service = item; expanded = false }) }
            }
        }
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(date, { date = it }, label = { Text("Preferred date (e.g. 25/11/2026) *") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(10.dp))
        Text("Preferred time", color = Ink, fontWeight = FontWeight.Medium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Morning", "Afternoon", "Evening").forEach { t ->
                FilterChip(selected = time == t, onClick = { time = t }, label = { Text(t) })
            }
        }
        OutlinedTextField(notes, { notes = it }, label = { Text("Additional notes (optional)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp), minLines = 3)
        Button(
            onClick = {
                if (name.isBlank() || phone.length < 10 || date.isBlank()) {
                    status = "Please enter your name, a valid mobile number and preferred date."
                } else {
                    sending = true
                    status = ""
                    try {
                        if (FirebaseApp.getApps(context).isEmpty()) {
                            sending = false
                            status = "Online booking is not connected yet. Add your clinic Firebase configuration (see README) to receive requests."
                        } else {
                            val data = hashMapOf(
                                "patientName" to name.trim(), "phone" to phone.trim(),
                                "service" to service, "preferredDate" to date.trim(),
                                "preferredTime" to time, "notes" to notes.trim(),
                                "createdAt" to com.google.firebase.Timestamp.now(),
                                "status" to "pending"
                            )
                            FirebaseFirestore.getInstance().collection("appointments").add(data)
                                .addOnSuccessListener {
                                    sending = false
                                    status = "Appointment request sent! The clinic will contact you to confirm."
                                    name = ""; phone = ""; date = ""; notes = ""
                                }
                                .addOnFailureListener {
                                    sending = false
                                    status = "Could not send your request. Please check your internet connection or call 70006637905."
                                }
                        }
                    } catch (e: Exception) {
                        sending = false
                        status = "Online booking setup is incomplete. Please call 70006637905."
                    }
                }
            },
            enabled = !sending,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (sending) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            else { Icon(Icons.Default.Send, null); Spacer(Modifier.width(8.dp)); Text("Submit Appointment Request") }
        }
        if (status.isNotBlank()) Text(status, color = if (status.startsWith("Appointment request sent")) Color(0xFF167544) else Color(0xFF9A4B00),
            modifier = Modifier.padding(top = 12.dp), fontSize = 14.sp)
        Text("Submitting a request does not confirm an appointment. The clinic must confirm your date and time.",
            color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 14.dp))
    }
}

@Composable
fun ContactScreen(padding: PaddingValues, onCall: () -> Unit, onMap: () -> Unit) {
    Column(Modifier.padding(padding).fillMaxSize().padding(18.dp)) {
        Text("Contact Hadi Dental Clinic", color = Navy, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        ContactRow(Icons.Default.Call, "Mobile", "70006637905")
        ContactRow(Icons.Default.LocationOn, "Address", "Ali Larijani Chowk, Sonapah, Beerwah – 193411")
        ContactRow(Icons.Default.Person, "Owned & managed by", "Barket Ali Malik (the dental hygienist)")
        Spacer(Modifier.height(18.dp))
        Button(onClick = onCall, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Call, null); Text("  Call Clinic") }
        OutlinedButton(onClick = onMap, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Map, null); Text("  Open Location") }
        Text("Healthy Teeth • Beautiful Smile • Better Life", color = Blue, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp))
    }
}

@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = Blue, modifier = Modifier.size(26.dp))
        Column(Modifier.padding(start = 12.dp)) {
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, color = Ink, fontWeight = FontWeight.SemiBold)
        }
    }
}
