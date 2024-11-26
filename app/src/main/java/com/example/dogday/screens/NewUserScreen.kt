package com.example.dogday.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogday.FirestoreInteractions
import com.example.dogday.R
import com.example.dogday.User
import com.example.dogday.UserSession
import com.example.dogday.ui.theme.BackgroundColorLight
import com.example.dogday.ui.theme.ButtonColorLight
import com.example.dogday.ui.theme.InputBackgroundLight
import com.example.dogday.viewmodel.NewUserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

@Composable
fun OwnerLabel() {
    Box(
        modifier = Modifier
            .size(width = 150.dp, height = 70.dp), // Juster høyden til ønsket størrelse
        contentAlignment = Alignment.Center
    ) {
        // Tegner en fylt form med avrundede hjørner
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color(0xFFD95A3C), // Farge på bakgrunnen
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                    16.dp.toPx(),
                    16.dp.toPx()
                ) // Avrundede hjørner
            )
        }

        // Legger til teksten "The owner" over formen
        Text(
            text = "The Owner",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold, // Setter teksten til fet
            fontSize = 25.sp, // Juster størrelsen etter behov
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewUserScreen(navController: NavController, newUserViewModel: NewUserViewModel = viewModel()) {
    val firstName by newUserViewModel.firstName
    val lastName by newUserViewModel.lastName
    val phoneNumber by newUserViewModel.phoneNumber
    val birthday by newUserViewModel.birthday
    val showDatePicker by newUserViewModel.showDatePicker
    val saveSuccess by newUserViewModel.saveSuccess

    // Create datePickerState locally in the composable
    val datePickerState = rememberDatePickerState()

    if (saveSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate("DogQueryScreen")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {

                OwnerLabel() // Legger til OwnerLabel her
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tell us more about you!",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold, // Gjør teksten bold
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = firstName,
            onValueChange = { newUserViewModel.firstName.value = it },
            label = { Text("First Name", color = Color.Black) },
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black

            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = lastName,
            onValueChange = { newUserViewModel.lastName.value = it },
            label = { Text("Last Name", color = Color.Black) },
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = phoneNumber,
            onValueChange = { newUserViewModel.phoneNumber.value = it },
            label = { Text("Phone Number", color = Color.Black) },
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = MaterialTheme.shapes.small
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center

        ) {
            OutlinedTextField(
                value = convertMillisToDate(birthday),
                onValueChange = { },
                label = { Text("Your Birthday",
                    Modifier
                        .background(color = Color(0xFFD95A3C))
                        .padding(5.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold) },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { newUserViewModel.showDatePicker.value = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.75f)
                    .height(64.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BackgroundColorLight,
                    unfocusedContainerColor = BackgroundColorLight,
                    focusedIndicatorColor = Color(0xFFD95A3C),
                    unfocusedIndicatorColor = Color(0xFFC0634D),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { newUserViewModel.showDatePicker.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            newUserViewModel.birthday.value = datePickerState.selectedDateMillis ?: 0L
                            newUserViewModel.showDatePicker.value = false
                        }) {
                            Text("OK", color = Color(0xFFC0634D),
                                fontWeight = FontWeight.Bold )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { newUserViewModel.showDatePicker.value = false }) {
                            Text("Cancel", color = Color(0xFFC0634D),
                                fontWeight = FontWeight.Bold)
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(
                                containerColor = Color(0xFFF3CCC3),
                                titleContentColor = Color.Black,
                                headlineContentColor = Color.Black,
                                navigationContentColor = Color.Black,
                                selectedYearContainerColor = Color(0xFFD95A3C),
                                selectedYearContentColor = Color.Black,
                                yearContentColor = Color.Black,
                                currentYearContentColor = Color.Black,
                                disabledSelectedYearContentColor = Color.Black,
                                dayContentColor = Color.Black,
                                disabledDayContentColor = Color.Black,
                                weekdayContentColor = Color.DarkGray,
                                subheadContentColor = Color.White,
                                selectedDayContentColor = Color.White,
                                selectedDayContainerColor = Color(0xFFD95A3C),
                                todayContentColor = Color.Black,
                                todayDateBorderColor = Color.Black,
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { newUserViewModel.saveUserData() },
            modifier = Modifier
                .width(150.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorLight
            ),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            Text(
                "Save",
                fontWeight = FontWeight.Bold
            )
        }
    }
}



