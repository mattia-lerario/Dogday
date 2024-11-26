package com.example.dogday.screens


import DogListViewModel
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dogday.R
import com.example.dogday.models.Dog
import com.example.dogday.models.VetNote
import kotlinx.coroutines.launch


@Composable
fun DogDetailScreen(navController: NavController, dogIdx: String) {
    val viewModel: DogListViewModel = viewModel()

    LaunchedEffect(dogIdx) {
        viewModel.fetchDog(dogIdx)
    }

    val dog by viewModel.dog.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        dog?.let { DogDetailUI(navController = navController, dog = it, viewModel) }
    }
}

@Composable
fun DogDetailUI(navController: NavController, dog: Dog, viewModel: DogListViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = dog.imageUrl ?: R.drawable.dog_cartoon,
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.dog_cartoon),
                error = painterResource(id = R.drawable.dog_cartoon),
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { navController.navigate("editDog/${dog.dogId}") }) {
                Text("Edit")
            }
        }

        Text(text = "Name: ${dog.name}", style = MaterialTheme.typography.titleLarge)
        Text(text = "Nickname: ${dog.nickName}")
        Text(text = "Breed: ${dog.breed}")
        Text(text = "Birthday: ${convertMillisToDate(dog.birthday)}")
        Text(text = "Breeder: ${dog.breeder}")
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "Event Log:", style = MaterialTheme.typography.titleLarge)

        VetLogNotes(
            dog = dog,
            onUpdateNote = { updatedNote -> viewModel.updateVetNoteForDog(dog, updatedNote) },
            onDeleteNote = { noteToDelete -> viewModel.deleteVetNoteForDog(dog, noteToDelete) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("addVetNote/${dog.dogId}")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Add Vet Note", fontWeight = FontWeight.Bold)
        }
    }
    }





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDogScreen(
    navController: NavController,
    dog: Dog,
    onSave: (Dog) -> Unit,
    onDelete: () -> Unit
) {
    var dogName by remember { mutableStateOf(dog.name) }
    var dogNickName by remember { mutableStateOf(dog.nickName) }
    var dogBreed by remember { mutableStateOf(dog.breed) }
    var dogBreeder by remember { mutableStateOf(dog.breeder) }
    var dogBirthday by remember { mutableStateOf(dog.birthday) }
    var dogImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val coroutineScope = rememberCoroutineScope()
    val viewModel: DogListViewModel = viewModel()

    val focusDogName = remember { FocusRequester() }
    val focusDogNickName = remember { FocusRequester() }
    val focusDogBreed = remember { FocusRequester() }
    val focusDogBreeder = remember { FocusRequester() }
    val focusDogBirthday = remember { FocusRequester() }

    var uploadingImage by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        dogImageBitmap = bitmap
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Your Dog's Details", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dogName,
            onValueChange = { dogName = it },
            label = { Text("Dog Name") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusDogName),
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
            onValueChange = { dogNickName = it },
            label = { Text("Dog Nick Name") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusDogNickName),
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
            onValueChange = { dogBreed = it },
            label = { Text("Dog Breed") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusDogBreed),
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
            onValueChange = { dogBreeder = it },
            label = { Text("Dog Breeder") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusDogBreeder),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusDogBirthday.requestFocus()
                }
            )
        )



        Spacer(modifier = Modifier.height(8.dp))

        TextField(
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
                .fillMaxWidth()
                .focusRequester(focusDogBirthday),
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
                        .verticalScroll(rememberScrollState())
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { cameraLauncher.launch(null) },
            modifier = Modifier.fillMaxWidth()
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
                coroutineScope.launch {
                    uploadingImage = true
                    var imageUrl = viewModel.getNewImageForDog(dogImageBitmap = dogImageBitmap, dog = dog)
                    uploadingImage = false

                    if (imageUrl != null) {
                        imageUrl = imageUrl
                    }
                    else
                    {
                        imageUrl = dog.imageUrl
                    }



                uploadingImage = true
                val dogToSave = dog.copy(
                    name = dogName,
                    nickName = dogNickName,
                    breed = dogBreed,
                    breeder = dogBreeder,
                    birthday = dogBirthday,
                    imageUrl = imageUrl
                )
                onSave(dogToSave)
                navController.popBackStack()
            }},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onDelete()
                navController.navigate(DogScreen.UserDogScreen.name)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Dog")
        }
    }
}




@Composable
fun VetLogNotes(
    dog: Dog,
    onUpdateNote: (VetNote) -> Unit,
    onDeleteNote: (VetNote) -> Unit
) {
    var selectedNote by remember { mutableStateOf<VetNote?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    if (dog.vetLog.isEmpty()) {
        Text(text = "No events recorded! Add one!")
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .height(400.dp)
        ) {
            items(dog.vetLog) { vetNote ->
                VetNoteItem(
                    vetNote = vetNote,
                    onClick = {
                        selectedNote = vetNote
                        showDialog = true
                    }
                )
            }
        }
    }

    if (showDialog && selectedNote != null) {
        EditVetNoteDialog(
            vetNote = selectedNote!!,
            onSave = {
                onUpdateNote(it)
                showDialog = false
            },
            onDelete = {
                onDeleteNote(selectedNote!!)
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}

@Composable
fun VetNoteItem(vetNote: VetNote, onClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = vetNote.note,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun EditVetNoteDialog(
    vetNote: VetNote,
    onSave: (VetNote) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var noteText by remember { mutableStateOf(vetNote.note) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Note") },
        text = {
            Column {
                TextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Write your note") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(vetNote.copy(note = noteText))
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDelete()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        }
    )
}


