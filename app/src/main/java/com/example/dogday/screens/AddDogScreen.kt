
package com.example.dogday.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.dogday.models.Dog
import com.example.dogday.FirestoreInteractions
import com.example.dogday.ui.theme.ButtonColorLight
import com.example.dogday.ui.theme.InputBackgroundLight
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.dogday.ui.theme.BackgroundColorLight
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


@Composable
fun YourDogLabel() {
    Box(
        modifier = Modifier
            .size(width = 150.dp, height = 70.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color(0xFFD95A3C),
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                    16.dp.toPx(),
                    16.dp.toPx()
                )
            )
        }

        // Legger til teksten "The owner" over formen
        Text(
            text = "Your Dog",
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
fun AddDogScreen(navController: NavController) {
    var dogName by remember { mutableStateOf("") }
    var dogNickName by remember { mutableStateOf("") }
    var dogBreed by remember { mutableStateOf("") }
    var dogBreeder by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var dogBirthday by remember { mutableStateOf(datePickerState.selectedDateMillis ?: 0L) }

    var dogImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var uploadingImage by remember { mutableStateOf(false) }

    val firestoreInteractions = FirestoreInteractions()
    val storage = FirebaseStorage.getInstance()

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        dogImageBitmap = bitmap
    }

    LaunchedEffect(Unit) {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
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

                YourDogLabel() // Legger til OwnerLabel her
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text("Let's add your dog!", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dogName,
            onValueChange = { dogName = it },
            label = { Text("Dog Name") },
            modifier = Modifier
                .widthIn(max = 500.dp)
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
            value = dogNickName,
            onValueChange = { dogNickName = it },
            label = { Text("Dog Nick Name") },
            modifier = Modifier
                .widthIn(max = 500.dp)
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
            value = dogBreed,
            onValueChange = { dogBreed = it },
            label = { Text("Dog Breed") },
            modifier = Modifier
                .widthIn(max = 500.dp)
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
            value = dogBreeder,
            onValueChange = { dogBreeder = it },
            label = { Text("Dog Breeder") },
            modifier = Modifier
                .widthIn(max = 500.dp)
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
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = convertMillisToDate(dogBirthday),
                onValueChange = { },
                label = { Text("Your Dog's Birthday", color = Color.Black) },
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
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.75f)
                    .height(64.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BackgroundColorLight,
                    unfocusedContainerColor = BackgroundColorLight,
                    focusedIndicatorColor = Color(0xFFD95A3C),
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            dogBirthday = datePickerState.selectedDateMillis ?: 0L
                            showDatePicker = false
                        }) {
                            Text("OK", color = Color.Black)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel", color = Color.Black)
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()) // Scroll kun her
                    ) {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(
                                containerColor = Color(0xFFF3CCC3),
                                titleContentColor = Color.Black,
                                headlineContentColor = Color.Black,
                                selectedYearContainerColor = Color(0xFFD95A3C),
                                selectedYearContentColor = Color.Black,
                                yearContentColor = Color.Black,
                                currentYearContentColor = Color.Black,
                                disabledSelectedYearContentColor = Color.Black,
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

        Button(
            onClick = {
                if (hasCameraPermission) {
                    cameraLauncher.launch(null)
                } else {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorLight
            ),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            Text("Take Picture")
        }

        Spacer(modifier = Modifier.height(16.dp))

            dogImageBitmap?.let { bitmap ->
                Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Dog Image", modifier = Modifier.size(200.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (dogName.isNotEmpty() && dogBreed.isNotEmpty() && dogImageBitmap != null) {
                        uploadingImage = true

                        val storageRef = storage.reference.child("dog_images/${dogName}_${System.currentTimeMillis()}.jpg")
                        val baos = ByteArrayOutputStream()
                        dogImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        val uploadTask = storageRef.putBytes(data)
                        uploadTask.addOnSuccessListener { taskSnapshot ->
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()

                                val uid = FirebaseAuth.getInstance().currentUser?.uid
                                if (uid != null) {
                                    val dog = Dog(
                                        name = dogName,
                                        nickName = dogNickName,
                                        breed = dogBreed,
                                        birthday = System.currentTimeMillis(),
                                        breeder = dogBreeder,
                                        imageUrl = imageUrl
                                )
                                firestoreInteractions.addDogToUser(uid, dog)

                                navController.navigate("home")
                            }
                        }
                    }.addOnFailureListener {
                        uploadingImage = false
                    }
                }
            },
            enabled = !uploadingImage,
            modifier = Modifier
                .width(200.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorLight
            ),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            Text(if (uploadingImage) "Uploading..." else "Add Dog")
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
