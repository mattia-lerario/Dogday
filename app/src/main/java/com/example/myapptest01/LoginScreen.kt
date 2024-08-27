package com.example.myapptest01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Din Skogsvenn På Tur", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = { navController.navigate("home") }) {
            Text("Logg inn")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("home") }) {
            Text("Registrer deg")
        }
    }
}
