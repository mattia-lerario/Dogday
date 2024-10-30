package com.example.dogday.screens


import DogListViewModel
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.example.dogday.R
import com.example.dogday.models.Dog
import com.example.dogday.models.VetNote


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

    ){
        dog?.let { DogDetailUI(navController = navController, dog = it) }

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
            AsyncImage(
                model = dog.imageUrl ?: R.drawable.dog_cartoon, // Bruk standardbilde hvis URL er null
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.dog_cartoon), // Mens laster vises standard-bilde
                error = painterResource(id = R.drawable.dog_cartoon), // Ved feil: Standardbilde
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
        }


        Text(text = "Navn: ${dog.name}")
        Text(text = "Kallenavn: ${dog.nickName}")
        Text(text = "Rase: ${dog.breed}")
        Text(text = "Bursdag: ${dog.birthday}")
        Text(text = "Oppdretter: ${dog.breeder}")
        Spacer(modifier = Modifier.height(26.dp))
        Text(text = "Hendelseslogg:")
        VetLogNotes(dog = dog)





    }}

@Composable
fun VetLogNotes(dog: Dog){
    if (dog.vetLog.isEmpty()) {
        Text(text = "Ingen hendelser registrert!" +
                " Trykk på + for å legge til!")
    }
    else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .height(400.dp)
        ) {
            items(dog.vetLog) { vetNote ->
                VetNoteItem(vetNote)

        }
    }

}}

@Composable
fun VetNoteItem(vetNote: VetNote) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)

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
            // Her kan du legge til andre detaljer om VetNote hvis tilgjengelig, som dato
        }
    }
}


