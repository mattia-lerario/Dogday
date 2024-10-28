package com.example.dogday.screens

import DogListViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.dogday.R
import com.example.dogday.models.Dog


@Composable
fun UserDogScreen(navController: NavHostController){

    val viewModel: DogListViewModel = viewModel()

    val dogs by viewModel.dogList.collectAsState()

    Column {
        for (dog in dogs) {
            DogX(navController = navController, dog)
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