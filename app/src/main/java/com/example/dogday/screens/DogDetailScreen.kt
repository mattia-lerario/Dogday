package com.example.dogday.screens


import DogListViewModel
import androidx.compose.foundation.Image

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.dogday.R
import com.example.dogday.models.Dog
import com.google.firebase.auth.FirebaseAuth



@Composable
fun DogDetailScreen(navController: NavController, dogIdx: String) {

    val viewModel: DogListViewModel = viewModel()

    LaunchedEffect(dogIdx) {
        viewModel.fetchDog(dogIdx)
    }

    val dog by viewModel.dog.collectAsState()

    dog?.let { DogDetailUI(navController = navController, dog = it) }
    
    if (dog == null) {
        Text(text = "Feil på lasting av hund!")
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
        Spacer(modifier = Modifier.height(26.dp))
        
        Text(text = "Knappen under skal snart virke slik at den gjør det mulig" +
                "å legge til logg fra dyrlege eller lignende!")



    }}


