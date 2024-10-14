package com.example.dogday

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun addVetLogScreen(navController: NavController, viewModel: DogViewModel = viewModel(), dogIdx: String){
    var vetName by remember { mutableStateOf("") }
    var vetNotes by remember { mutableStateOf("") }

    val firestoreInteractions = FirestoreInteractions()

    LaunchedEffect(dogIdx) {
        viewModel.fetchDogById(dogIdx)
    }

    val dog by viewModel.dog.collectAsState()
    val localDog = dog

    if (localDog != null) {
        Column() {
            Text(text = "Hei")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Legg til vet-notater", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = vetName,
            onValueChange = { vetName = it },
            label = { Text("Vet navn") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = vetNotes,
            onValueChange = { vetNotes = it },
            label = { Text("Notater") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (vetName.isNotEmpty() && vetNotes.isNotEmpty()) {
                // Get the current user's UID
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    // Create a Dog object
                    val updatedDog = localDog?.copy(
                        vetLog = VetLog(dog = localDog, vetName = vetName, date = null, notes = vetNotes)
                    )
                    if (updatedDog != null) {
                        firestoreInteractions.addDogToUser(uid, updatedDog)
                    }

                    navController.navigate(route = DogScreen.Home.name)
                } else {
                    // Handle error: user UID is null
                }
            }
        }) {
            Text("Legg til notat")
        }
    }



    Text(text = "Hei")




}