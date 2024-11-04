package com.example.dogday.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dogday.models.DogRecommendation
import com.example.dogday.repository.DogRecommendationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendedDogListScreen(navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = DogRecommendationRepository(firestore)
    var dogList by remember { mutableStateOf(listOf<DogRecommendation>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            dogList = repository.fetchAllDogs()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(dogList) { dog ->
            DogListItem(dog = dog, onClick = {
                navController.navigate("dog_detail/${dog.breed}")
            })
        }
    }
}

@Composable
fun DogListItem(dog: DogRecommendation, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick,
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(
                    text = dog.breed,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp)
                )
                Text(
                    text = dog.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Image(
                painter = rememberAsyncImagePainter(dog.imageUrl),
                contentDescription = dog.breed,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .padding(end = 8.dp)
            )
        }
    }
}
