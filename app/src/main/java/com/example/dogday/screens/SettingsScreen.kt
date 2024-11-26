package com.example.dogday.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dogday.viewmodel.LogInViewModel

@Composable
fun SettingsScreen(navController: NavController, logInViewModel: LogInViewModel = viewModel()) {
    val context = LocalContext.current

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
                FloatingActionButton(
                    onClick = {
                        logInViewModel.logoutUser(context)

                        navController.navigate(DogScreen.Login.name) {
                            popUpTo(0)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Logout", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
