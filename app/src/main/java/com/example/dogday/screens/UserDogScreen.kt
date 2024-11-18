package com.example.dogday.screens

import DogListViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.dogday.R
import com.example.dogday.models.Dog


@Composable
fun UserDogScreen(navController: NavHostController) {
    val viewModel: DogListViewModel = viewModel()
    val dogs by viewModel.dogList.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            for (dog in dogs) {
                DogX(navController = navController, dog)
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(DogScreen.AddDog.name) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(15.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Dog")
        }
    }
}

@Composable
fun DogX(navController: NavHostController, dog: Dog){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp),
        onClick = { navController.navigate("DogDetailScreen/${dog.dogId}")},
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(5.dp)) {
                Text(text = "${dog.name}", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp))
                //Text(text = "${dog.breed}")
            }

            AsyncImage(
                model = dog.imageUrl ?: R.drawable.dog_cartoon, // Bruk standardbilde hvis URL er null
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.dog_cartoon), // Mens laster vises standard-bilde
                error = painterResource(id = R.drawable.dog_cartoon), // Ved feil: Standardbilde
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )


        }

    }


}