
package com.example.dogday.screens

import DogListViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogday.ui.theme.BackgroundColorLight
import com.example.dogday.ui.theme.ButtonColorLight
import com.example.dogday.ui.theme.InputBackgroundLight
import com.example.dogday.viewmodel.AddDogViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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

    val datePickerState = rememberDatePickerState()

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            addDogViewModel.onDogImageCaptured(bitmap)
        } else {
            println("Failed to capture image or camera was cancelled")
        }
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Dog", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    YourDogLabel() // Header Label
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Let's add your dog!", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            // Dog Name Input
            TextField(
                value = dogName,
                onValueChange = { addDogViewModel.onDogNameChange(it) },
                label = { Text("Dog Name", color = Color.Black) },
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

            // Dog Nick Name Input
            TextField(
                value = dogNickName,
                onValueChange = { addDogViewModel.onDogNickNameChange(it) },
                label = { Text("Dog Nick Name", color = Color.Black) },
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

            // Dog Breed Input
            TextField(
                value = dogBreed,
                onValueChange = { addDogViewModel.onDogBreedChange(it) },
                label = { Text("Dog Breed", color = Color.Black) },
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

            // Dog Breeder Input
            TextField(
                value = dogBreeder,
                onValueChange = { addDogViewModel.onDogBreederChange(it) },
                label = { Text("Dog Breeder", color = Color.Black) },
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

            // Dog Birthday Input
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    value = convertMillisToDate(dogBirthday),
                    onValueChange = { },
                    label = {
                        Text(
                            "Your Dog's Birthday",
                            Modifier.background(color = Color(0xFFD95A3C)).padding(5.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
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
                        .height(64.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = BackgroundColorLight,
                        unfocusedContainerColor = BackgroundColorLight,
                        focusedIndicatorColor = Color(0xFFD95A3C),
                        unfocusedIndicatorColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Dog Button
            Button(
                onClick = { addDogViewModel.saveDogData() },
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
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
