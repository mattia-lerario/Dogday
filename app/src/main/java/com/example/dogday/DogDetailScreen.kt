package com.example.dogday


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


//Forsøk på å lagre data fra Firebase i en viewModel

class DogViewModel : ViewModel() {
    private val _dog = MutableStateFlow<Dog?>(null)  // Holder én enkelt hund
    val dog: StateFlow<Dog?> get() = _dog

    fun fetchDogById(dogIdx: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

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

                                if (dogId != null && name != null && breed != null && dogIdx == dogId ) {
                                    val fetchedDog = Dog(
                                        dogId = dogId,
                                        name = name,
                                        breed = breed
                                    )
                                    _dog.value = fetchedDog
                                } else {
                                    _dog.value = null
                                }
                            }
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

    fun addVetLog(dogIdx: String){
        //Hmmm...
    }
}




@Composable
fun DogDetailScreen2(viewModel: DogViewModel = viewModel(), dogIdx: String) {
    LaunchedEffect(dogIdx) {
        viewModel.fetchDogById(dogIdx)
    }

    val dog by viewModel.dog.collectAsState()
    val localDog = dog

    if (localDog != null) {
        Column() {
            DogDetailUI(navController = rememberNavController(),dog = localDog)
        }
    }
}

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
                                val dogId = dogData["dogId"] as? String
                                val name = dogData["name"] as? String
                                val breed = dogData["breed"] as? String

                                if (dogId != null && name != null && breed != null && dogIdx == dogId ) {
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
              DogDetailUI(navController, dog = dog)

            }
        }
    } else {
        Text(text = "You haven't added any dogs yet.")
    }
}





@Composable
fun DogDetailUI(navController : NavController ,dog: Dog) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dog),
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .padding(0.dp)

            )
        }


            Text(text = "Navn: ${dog.name}")
            Text(text = "Rase: ${dog.breed}")
            Text(text = "DogId: ${dog.dogId}")
            Text(text = "VetLog: ${dog.vetLog}")
            Button(onClick = { navController.navigate("addVetLogScreen/${dog.dogId}")}) {

            }


/*
        if (dog.vetLog == null){
            Text(text = "Ingen registrert veterinærlogg.")
        }
        else {
            vetList = emptyList<>()

            LazyColumn {
                items(vetList) {vetLog ->
                    Text(text="${vetLog.date}")
                }

            }

        Text(text = "{dog.vetLog}")
*/
//navController.navigate(route = DogScreen.AddVetLog.name)



}


    }
//}








