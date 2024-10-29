package com.example.dogday.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.dogday.R
import com.example.dogday.models.DogID
import com.example.dogday.models.DogRecommendation
import com.example.dogday.repository.DogRecommendationRepository
import com.example.dogday.ui.theme.ButtonColorLight
import com.example.dogday.viewmodel.DogQuizViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DogQuizResultsScreen(
    navController: NavController,
    dogID: String
) {
    // Initialize Firestore and Repository
    val firestore = FirebaseFirestore.getInstance()
    val repository = DogRecommendationRepository(firestore)

    // Directly initialize DogQuizViewModel with remember
    val viewModel = remember { DogQuizViewModel(repository) }

    // Collect state from the ViewModel
    val dogRecommendations by viewModel.dogRecommendations.collectAsState()

    // Log to check if dogID and dogRecommendations are populated correctly

    // Retrieve the recommended dog based on the dogID
    val recommendedDog: DogRecommendation? = dogRecommendations[DogID.valueOf(dogID)]

    if (recommendedDog == null) {
        // Show a loading or error state if data is null
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Loading recommendation...",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    } else {
        // Display the recommended dog
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Recommended Breed",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Image(
                painter = rememberAsyncImagePainter(
                    model = recommendedDog.imageUrl,
                    onError = { println("Error loading image for ${recommendedDog.breed}: ${it.result.throwable.message}") },
                    onSuccess = { println("Image loaded successfully for ${recommendedDog.breed}") }
                ),
                contentDescription = recommendedDog.breed,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )

            println("Image URL for ${recommendedDog.breed}: ${recommendedDog.imageUrl}")


            Text(
                text = recommendedDog.breed,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = recommendedDog.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate(route = DogScreen.Home.name) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColorLight),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text("Go Home", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}