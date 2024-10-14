
package com.example.dogday.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.dogday.models.Dog
import com.example.dogday.FirestoreInteractions
//import com.example.dogday.models.Dog
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDogScreen(navController: NavController) {
    var dogName by remember { mutableStateOf("") }
    var dogNickName by remember { mutableStateOf("") }
    var dogBreed by remember { mutableStateOf("") }
    var dogBreeder by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var dogBirthday by remember { mutableStateOf(datePickerState.selectedDateMillis ?: 0L) }


    val firestoreInteractions = FirestoreInteractions()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Looks like you haven't added your dogs, lets add one!", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dogName,
            onValueChange = { dogName = it },
            label = { Text("Dog Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = dogNickName,
            onValueChange = { dogNickName = it },
            label = { Text("Dog Nick Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = dogBreed,
            onValueChange = { dogBreed = it },
            label = { Text("Dog Breed") },
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = convertMillisToDate(dogBirthday),
                onValueChange = { },
                label = { Text("Dog's Birthday") },
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
                            dogBirthday = datePickerState.selectedDateMillis ?: 0L
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

            //if (showDatePicker) {
                //Popup(
                    //onDismissRequest = { showDatePicker = false},
                    //alignment = Alignment.TopStart
                //){
                   // Box(
                        //modifier = Modifier
                            //.fillMaxWidth()
                            //.offset(y = 64.dp)
                            //.shadow(elevation = 4.dp)
                            //.background(MaterialTheme.colorScheme.surface)
                            //.padding(16.dp)
                   // ){
                        //Column {
                            //DatePicker(
                                //state = datePickerState,
                                //showModeToggle = false
                            //)

                            //Spacer(modifier = Modifier.height(8.dp))

                            //Row(
                                //modifier = Modifier.fillMaxWidth(),
                                //horizontalArrangement = Arrangement.End
                            //) {
                                //TextButton(onClick = {
                                    //dogBirthday = datePickerState.selectedDateMillis ?: 0L
                                    //showDatePicker = false
                                //}) {
                                    //Text("Ok")
                                //}
                                //TextButton(onClick = { showDatePicker = false}) {
                                    //Text("Cancel")
                                //}
                            //}


                        //}
                    //}
                //}
            //}
        }

        Spacer(modifier = Modifier.height(8.dp))


        TextField(
            value = dogBreeder,
            onValueChange = { dogBreeder = it },
            label = { Text("Dog Breeder") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (dogName.isNotEmpty() && dogBreed.isNotEmpty()) {
                // Get the current user's UID
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    // Create a Dog object
                    val dog = Dog(
                        name = dogName,
                        nickName = dogNickName,
                        breed = dogBreed,
                        birthday = dogBirthday,
                        breeder = dogBreeder
                        )
                    firestoreInteractions.addDogToUser(uid, dog)

                    navController.navigate("home")
                } else {
                    // Handle error: user UID is null
                }
            }
        }) {
            Text("Add Dog")
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
