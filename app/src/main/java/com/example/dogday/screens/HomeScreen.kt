package com.example.dogday.screens

import DogListViewModel
import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.dogday.R



@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel: DogListViewModel = viewModel()
    val numOfDogs = viewModel.numberOfDogs()


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
            if (viewModel.dogList.value.isEmpty()) {
                NotDogOwnerCard(navController = navController)
                BrowseBreedsCard(navController = navController)
            } else {
                DogCard(navController = navController, numOfDogs)
            }
            HikeCard(navController = navController)

        }

        if (viewModel.dogList.value.isEmpty()) {
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
}

@Composable
fun HikeCard(navController: NavHostController){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp),
        onClick = { navController.navigate(DogScreen.Map.name)},
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {

            Image(
                painter = painterResource(id = R.drawable.dog_cartoon),
                contentDescription = "Dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .padding(0.dp)
            )

            Column(modifier = Modifier
                .weight(1f)
                .padding(10.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = "Ready for a hike?", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp))
                Text(text = "Discover exciting hikes here!")
            }


        }

    }
}


@Composable
fun DogCard(navController: NavHostController, numberOfDogs: Int){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp),
        onClick = { navController.navigate(DogScreen.UserDogScreen.name)},
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(5.dp)) {
                Text(text = if (numberOfDogs > 1) "Your dogs" else "Your dog", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp))
                Text(text = "Click here to go to your overview!")
            }

            Image(
                painter = painterResource(id = R.drawable.dog_cartoon),
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
fun NotDogOwnerCard(navController: NavHostController){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 6.dp),
        onClick = { navController.navigate(DogScreen.DogQueryScreen.name)},
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(5.dp)) {
                Text(text = "Take the Dog Quiz", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp))
                Text(text = "Find the breed for you!")
            }

            Image(
                painter = painterResource(id = R.drawable.dog_cartoon),
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
fun BrowseBreedsCard(navController: NavHostController) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = { navController.navigate("dog_list") },
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(
                    text = "Browse Dog Breeds",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(0.dp)
                )
                Text(text = "Find and explore different dog breeds!")
            }

            Image(
                painter = painterResource(id = R.drawable.dog_cartoon),
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






