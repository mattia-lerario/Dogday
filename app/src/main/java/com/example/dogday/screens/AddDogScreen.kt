
package com.example.dogday.screens

import DogListViewModel
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogday.ui.theme.ButtonColorLight
import com.example.dogday.ui.theme.InputBackgroundLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogday.ui.theme.BackgroundColorLight
import com.example.dogday.viewmodel.AddDogViewModel


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
fun AddDogScreen(
    navController: NavController,
    addDogViewModel: AddDogViewModel = viewModel(),
    dogListViewModel: DogListViewModel = viewModel()
) {
    val dogName by addDogViewModel.dogName.collectAsState()
    val dogNickName by addDogViewModel.dogNickName.collectAsState()
    val dogBreed by addDogViewModel.dogBreed.collectAsState()
    val dogBreeder by addDogViewModel.dogBreeder.collectAsState()
    val dogBirthday by addDogViewModel.dogBirthday.collectAsState()
    val showDatePicker by addDogViewModel.showDatePicker.collectAsState()
    val saveSuccess by addDogViewModel.saveSuccess.collectAsState()
    val dogImageBitmap by addDogViewModel.dogImageBitmap.collectAsState()
    val uploadingImage by addDogViewModel.uploadingImage.collectAsState()



    val focusDogName = remember { FocusRequester() }
    val focusDogNickName = remember { FocusRequester() }
    val focusDogBreed = remember { FocusRequester() }
    val focusDogBreeder = remember { FocusRequester() }
    val focusDogBirthday = remember { FocusRequester() }




    val datePickerState = rememberDatePickerState()

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            addDogViewModel.onDogImageCaptured(bitmap)
        } else {
            println("Failed to capture image or camera was cancelled")
        }
    }

    // Oppdater requestCameraPermissionLauncher til å starte kamera når tillatelse gis
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Start kamera når tillatelse er gitt
            cameraLauncher.launch(null)
        } else {
            println("Camera permission denied")
        }
    }

    if (saveSuccess) {
        LaunchedEffect(Unit) {
            dogListViewModel.fetchDogs()
            navController.navigate("home")
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

                YourDogLabel() // Legger til OwnerLabel her
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text("Let's add your dog!", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dogName,
            onValueChange = { addDogViewModel.onDogNameChange(it) },
            label = { Text("Dog Name", color = Color.Black) },
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(horizontal = 8.dp)
                .focusRequester(focusDogName),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                    ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusDogNickName.requestFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = dogNickName,
            onValueChange = { addDogViewModel.onDogNickNameChange(it) },
            label = { Text("Dog Nick Name", color = Color.Black) },
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(horizontal = 8.dp)
                .focusRequester(focusDogNickName),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusDogBreed.requestFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = dogBreed,
            onValueChange = { addDogViewModel.onDogBreedChange(it) },
            label = { Text("Dog Breed", color = Color.Black) },
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(horizontal = 8.dp)
                .focusRequester(focusDogBreed),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusDogBreeder.requestFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = dogBreeder,
            onValueChange = { addDogViewModel.onDogBreederChange(it) },
            label = { Text("Dog Breeder", color = Color.Black) },
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(horizontal = 8.dp)
                .focusRequester(focusDogBreeder),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackgroundLight,
                unfocusedContainerColor = InputBackgroundLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusDogBirthday.requestFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = convertMillisToDate(dogBirthday),
                onValueChange = { },
                label = { Text("Your Dog's Birthday",
                    Modifier.background(color = Color(0xFFD95A3C)).padding(5.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold) },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { addDogViewModel.toggleDatePicker() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.75f)
                    .height(64.dp)
                    .focusRequester(focusDogBirthday),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BackgroundColorLight,
                    unfocusedContainerColor = BackgroundColorLight,
                    focusedIndicatorColor = Color(0xFFD95A3C),
                    unfocusedIndicatorColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { addDogViewModel.toggleDatePicker() },
                    confirmButton = {
                        TextButton(onClick = {
                            addDogViewModel.onDogBirthdayChange(datePickerState.selectedDateMillis ?: 0L)
                            addDogViewModel.toggleDatePicker()
                        }) {
                            Text("OK", color = Color(0xFFC0634D))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { addDogViewModel.toggleDatePicker() }) {
                            Text("Cancel", color = Color(0xFFC0634D))
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

        Button(
            onClick = {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
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
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Dog Image",
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {addDogViewModel.saveDogData()},
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
