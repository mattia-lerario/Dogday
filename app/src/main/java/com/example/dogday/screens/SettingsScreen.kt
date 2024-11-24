package com.example.dogday.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogday.repository.BreederRepository
import com.example.dogday.repository.KennelRepository
import com.example.dogday.api.SearchPlaces

@Composable
fun SettingsScreen(navController: NavController) {
    val kennelRepository = KennelRepository()
    val breederRepository = BreederRepository()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(onClick = { navController.navigate(route = DogScreen.AddDog.name) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Dog")
                }
                Text(text = "Add dog")
            }

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(onClick = { navController.navigate(route = DogScreen.DogQueryScreen.name) }) {
                    Icon(Icons.Default.Lightbulb, contentDescription = "Quiz")
                }
                Text(text = "DogQuiz")
            }

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(onClick = { navController.navigate(route = DogScreen.Login.name) }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                }
                Text(text = "Logout")
            }
        }

        // Buttons for fetching Kennels and Breeders
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(onClick = {
                // Fetch kennels from Google Places API and upload to Firestore
                SearchPlaces.searchKennelsByKeyword(
                    onSuccess = { kennels ->
                        Log.d("SettingsScreen", "Kennels fetched: $kennels")
                        kennelRepository.uploadKennelsToFirestore(kennels)
                    },
                    onFailure = { exception ->
                        Log.e("SettingsScreen", "Error fetching kennels: ${exception.message}")
                    }
                )
            }) {
                Text("Fetch Kennels")
            }

            Spacer(modifier = Modifier.height(16.dp))

            FloatingActionButton(onClick = {
                // Fetch breeders from Google Places API and upload to Firestore
                SearchPlaces.searchBreedersByKeyword(
                    onSuccess = { breeders ->
                        Log.d("SettingsScreen", "Breeders fetched: $breeders")
                        breederRepository.uploadBreedersToFirestore(breeders)
                    },
                    onFailure = { exception ->
                        Log.e("SettingsScreen", "Error fetching breeders: ${exception.message}")
                    }
                )
            }) {
                Text("Fetch Breeders")
            }
        }
    }
}
