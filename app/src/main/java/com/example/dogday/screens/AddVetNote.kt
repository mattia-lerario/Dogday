package com.example.dogday.screens
import DogListViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogday.models.Dog
import com.example.dogday.models.VetNote


@Composable
fun VetNoteScreen(
    navController: NavController,
    dogId: String,
    vetNote: VetNote? = null,
    onSaveNote: (VetNote) -> Unit,
    onDeleteNote: ((VetNote) -> Unit)? = null
) {
    var noteText by remember { mutableStateOf(vetNote?.note ?: "") }
    val viewModel: DogListViewModel = viewModel()

    LaunchedEffect(dogId) {
        viewModel.fetchDog(dogId)
    }

    val dog by viewModel.dog.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (vetNote == null) "Add Note" else "Edit Note",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text("Write your note here") },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            maxLines = 6,
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val updatedNote = VetNote(note = noteText)
                dog?.let {
                    onSaveNote(updatedNote)
                }
            }) {
                Text(if (vetNote == null) "Save" else "Update")
            }

            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel")
            }
            if (vetNote != null && onDeleteNote != null) {
                Button(
                    onClick = {
                        onDeleteNote(vetNote)
                        //navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
