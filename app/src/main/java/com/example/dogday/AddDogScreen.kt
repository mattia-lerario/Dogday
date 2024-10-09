
package com.example.dogday

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogday.models.Dog
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddDogScreen(navController: NavController) {
    var dogName by remember { mutableStateOf("") }
    var dogNickName by remember { mutableStateOf("") }
    var dogBirthday by remember { mutableStateOf("") }
    var dogBreed by remember { mutableStateOf("") }
    var dogBreeder by remember { mutableStateOf("") }

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

        TextField(
            value = dogBirthday,
            onValueChange = { dogBirthday = it },
            label = { Text("Your Dog's Birthday") },
            modifier = Modifier.fillMaxWidth()
        )

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
