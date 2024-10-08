package com.example.dogday.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dogday.Dog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Dine hunder", style = MaterialTheme.typography.bodyLarge)

        DogsList()

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Finn Din Neste Tur", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("map") }) {
            Text("Se anbefalinger")
        }
    }
}

@Composable
fun DogsList() {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    var dogsList by remember { mutableStateOf<List<Dog>>(emptyList()) }

    LaunchedEffect(uid) {
        if (uid != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("ddcollection").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val dogsMap = document.get("dogs") as? Map<*, *>

                        if (dogsMap != null) {
                            val dogs = dogsMap.values.mapNotNull { dogData ->
                                val dogInfo = dogData as? Map<*, *>
                                val dogId = dogInfo?.get("dogId") as? String
                                val name = dogInfo?.get("name") as? String
                                val breed = dogInfo?.get("breed") as? String

                                if (dogId != null && name != null && breed != null) {
                                    Dog(
                                        dogId = dogId,
                                        name = name,
                                        breed = breed
                                    )
                                } else {
                                    null
                                }
                            }
                            dogsList = dogs
                        }
                    } else {
                        println("Document does not exist")
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error fetching document: ${exception.message}")
                }
        }
    }

    if (dogsList.isNotEmpty()) {
        LazyColumn {
            items(dogsList) { dog ->
                Text(text = "Dog: ${dog.name}")
                Text(text = "Breed: ${dog.breed}")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    } else {
        Text(text = "You haven't added any dogs yet.")
    }
}
