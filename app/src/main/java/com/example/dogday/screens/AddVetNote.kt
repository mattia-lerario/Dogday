package com.example.dogday.screens
import DogListViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogday.models.Dog
import com.example.dogday.models.VetNote


@Composable
fun VetNoteScreen(navController: NavController) {
    var noteText by remember { mutableStateOf("") }
    val viewModel: DogListViewModel = viewModel()
    val currentDog by viewModel.dog.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Legg til veterinærnotat", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text("Beskrivelse") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val vetNote = VetNote(note = noteText)

                currentDog?.let {
                    viewModel.addNoteToDog(it, vetNote,
                        onSuccess = { navController.popBackStack() },
                        onFailure = { exception ->

                            println("Error adding note: ${exception.message}")
                        }
                    )
                }
            }) {
                Text("Lagre")
            }

            Button(onClick = { navController.popBackStack() }) {  // Bruker navController direkte for å gå tilbake
                Text("Avbryt")
            }
        }
    }
}
