package com.example.dogday.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogday.FirestoreInteractions
import com.example.dogday.R
import com.example.dogday.User
import com.example.dogday.UserSession
import com.example.dogday.ui.theme.ButtonColorLight
import com.example.dogday.ui.theme.InputBackgroundLight
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUserScreen(navController: NavController) {
    val email = UserSession.email ?: ""
    val uid = UserSession.uid ?: ""

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var hasDog by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var birthday by remember { mutableStateOf(datePickerState.selectedDateMillis ?: 0L) }

    val firestoreInteractions = FirestoreInteractions()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.dogday_logo),
                    contentDescription = "DogDay Logo",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )

                OwnerLabel() // Legger til OwnerLabel her
            }
        }

        Text(
            text = "Tell us more about you!",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name", color = Color.Black) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = convertMillisToDate(birthday),
                onValueChange = { },
                label = { Text("Your Birthday") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            birthday = datePickerState.selectedDateMillis ?: 0L
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (firstName.isNotEmpty() && phoneNumber.isNotEmpty()) {
                val user = User(
                    uid,
                    email,
                    firstName,
                    lastName,
                    phoneNumber,
                    birthday
                )
                firestoreInteractions.addUser(user)
                val uid = Firebase.auth.currentUser?.uid
                if (uid != null) {
                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection("ddcollection").document(uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val user = document.toObject(User::class.java)
                                if (user != null) {
                                    if (hasDog) {
                                        navController.navigate("addDogScreen")
                                    } else {
                                        navController.navigate("home")
                                    }
                                }
                            }
                        }
                }
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorLight
            ),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            Text(
                "Save information",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OwnerLabel() {
    Box(
        modifier = Modifier
            .size(width = 150.dp, height = 100.dp), // Juster høyden til ønsket størrelse
        contentAlignment = Alignment.Center
    ) {
        // Tegner en fylt form med avrundede hjørner
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color(0xFFD95A3C), // Farge på bakgrunnen
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx()) // Avrundede hjørner
            )
        }

        // Legger til teksten "The owner" over formen
        Text(
            text = "The owner",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold, // Setter teksten til fet
            fontSize = 20.sp, // Juster størrelsen etter behov
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// Preview-funksjon for OwnerLabel
@Preview(showBackground = true)
@Composable
fun PreviewOwnerLabel() {
    OwnerLabel()
}
