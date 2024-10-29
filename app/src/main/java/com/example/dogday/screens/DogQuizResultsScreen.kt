package com.example.dogday.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogday.models.DogID
import com.example.dogday.models.dogRecommendations
import com.example.dogday.ui.theme.ButtonColorLight

@Composable
fun DogQuizResultsScreen(navController: NavController, dogID: String) {
    val recommendedDog = dogRecommendations[DogID.valueOf(dogID)] ?: dogRecommendations[DogID.BULLDOG]!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Recommended Dog Breed",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Image(
            painter = painterResource(id = recommendedDog.imageResId),
            contentDescription = recommendedDog.breed,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit
        )

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