package com.example.dogday.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun SettingsScreen(navController: NavController) {
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
                    // You could add an Icon here if needed
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
                FloatingActionButton(onClick = { navController.navigate(route = DogScreen.Login.name) }) {

                }
                Text(text = "Login")
            }

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(onClick = { navController.navigate(route = DogScreen.DogQueryScreen.name) }) {

                }
                Text(text = "DogQuiz")
            }
        }

        // Adding Logout Button
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(onClick = {

                navController.navigate(route = DogScreen.Login.name)
            }) {

            }
            Text(text = "Logout")
        }
    }
}


