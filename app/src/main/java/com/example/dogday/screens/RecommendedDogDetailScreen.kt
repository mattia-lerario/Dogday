package com.example.dogday.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dogday.models.DogRecommendation
import com.example.dogday.repository.DogRecommendationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Suppress("UNUSED_PARAMETER")
@Composable
fun RecommendedDogDetailScreen(navController: NavController, breed: String) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = DogRecommendationRepository(firestore)
    var dog by remember { mutableStateOf<DogRecommendation?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(breed) {
        coroutineScope.launch {
            dog = repository.fetchDogByBreed(breed)
        }
    }

    dog?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(it.imageUrl),
                contentDescription = it.breed,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = it.breed,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = it.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading dog details...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}