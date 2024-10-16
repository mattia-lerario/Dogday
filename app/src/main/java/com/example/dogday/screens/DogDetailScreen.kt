package com.example.dogday.screens


import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import com.example.dogday.R
import com.example.dogday.models.Dog
import com.google.firebase.auth.FirebaseAuth


//Kode under er kopiert fra J, kun gjort endring for å hente kun der dogId er lik.
@Composable
fun DogDetailScreen(navController : NavController ,dogIdx: String) {
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
                                val dogInfo = dogData as? Map<*, *>
                                val dogId = dogInfo?.get("dogId") as? String
                                val name = dogInfo?.get("name") as? String
                                val breed = dogInfo?.get("breed") as? String
                                val nickName = dogInfo?.get("nickName") as? String
                                val birthday = dogInfo?.get("birthday") as? Long
                                val breeder = dogInfo?.get("breeder") as? String

                                if (dogId != null && name != null && breed != null && dogIdx == dogId) {
                                    Dog(
                                        dogId = dogId,
                                        name = name,
                                        nickName = nickName ?: "",
                                        breed = breed,
                                        birthday = birthday ?: 0L,
                                        breeder = breeder ?: ""
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
                DogDetailUI(navController, dog = dog)

            }
        }
    } else {
        Text(text = "You haven't added any dogs yet.")
    }
}





@Composable
fun DogDetailUI(navController : NavController, dog: Dog) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dog_cartoon),
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .padding(0.dp)

            )
        }


        Text(text = "Navn: ${dog.name}")
        Text(text = "Kallenavn: ${dog.nickName}")
        Text(text = "Rase: ${dog.breed}")
        Text(text = "Bursdag: ${dog.birthday}")
        Text(text = "Oppdretter: ${dog.breeder}")



    }}


