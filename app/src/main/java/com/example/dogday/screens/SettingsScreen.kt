package com.example.dogday.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
<<<<<<< Updated upstream
                FloatingActionButton(onClick = { navController.navigate(route = DogScreen.AddDog.name) }) {
                    // You could add an Icon here if needed
                }
                Text(text = "Legg til hund")
            }

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(onClick = { navController.navigate(route = DogScreen.Login.name) }) {
                    // You could add an Icon here if needed
                }
                Text(text = "Login")
=======
            FloatingActionButton(onClick = { navController.navigate(route = DogScreen.AddDog.name) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Dog")
            }
            Text(text = "Add dog")
>>>>>>> Stashed changes
            }

            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(onClick = { navController.navigate(route = DogScreen.DogQueryScreen.name) }) {
<<<<<<< Updated upstream
                    // You could add an Icon here if needed
=======
                    Icon(Icons.Default.Lightbulb, contentDescription = "Quiz")

>>>>>>> Stashed changes
                }
                Text(text = "DogQ")
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


        /*Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(), // Fill the rest of the screen vertically
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(onClick = {
                // Add logout logic here
                // For example, navigate to Login screen after logout or perform Firebase sign out
                navController.navigate(route = DogScreen.Login.name)
            }) {
                // You could add an Icon here if needed
            }
            Text(text = "Logout")
       }*/
    }

    }



