package com.example.dogday


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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

        DogsList(navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Finn Din Neste Tur", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = { navController.navigate(route = DogScreen.Map.name) }) {
            Text("Se anbefalinger")
        }
    }
}

@Composable
fun DogListCard(navController: NavHostController, dog: Dog){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp),
        onClick = { navController.navigate("DogDetailScreen/${dog.dogId}")},
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier.weight(1f)
                .padding(10.dp)) {
                Text(text = "${dog.name}",
                    modifier = Modifier.padding(0.dp))
                Text(text = "${dog.breed}")
            }

            Image(
                painter = painterResource(id = R.drawable.dog),
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .padding(0.dp)
            )


        }

    }
}




@Composable
fun DogsList(navController: NavHostController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    var dogsList by remember { mutableStateOf<List<Dog>>(emptyList()) }

    LaunchedEffect(uid) {
        if (uid != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("ddcollection").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val dogsMap = document.get("dogs") as? Map<String, Map<String, Any>>


                        if (dogsMap != null) {
                            val dogs = dogsMap.values.mapNotNull { dogData ->
                                val dogId = dogData["dogId"] as? String
                                val name = dogData["name"] as? String
                                val breed = dogData["breed"] as? String

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
                DogListCard(navController = navController, dog = dog)
                Spacer(modifier = Modifier.height(16.dp))



            }
        }
    } else {
        Text(text = "You haven't added any dogs yet.")
    }
}
