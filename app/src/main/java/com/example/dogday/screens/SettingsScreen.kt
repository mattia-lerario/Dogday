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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun SettingsScreen(navController: NavController){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Row() {
            Column(modifier = Modifier.padding(5.dp)
                .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,  // Sentrerer innholdet horisontalt
                verticalArrangement = Arrangement.Center) {
                FloatingActionButton(onClick = {navController.navigate(route = DogScreen.AddDog.name)}) {
                }
                Text(text = "Legg til hund")
            }

            Column(modifier = Modifier.padding(5.dp)
                .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,  // Sentrerer innholdet horisontalt
                verticalArrangement = Arrangement.Center)
             {
                FloatingActionButton(onClick = {navController.navigate(route = DogScreen.Login.name)}) {
                }
                Text(text = "Login")
            }
            Column(modifier = Modifier.padding(5.dp)
                .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,  // Sentrerer innholdet horisontalt
                verticalArrangement = Arrangement.Center) {
                FloatingActionButton(onClick = {navController.navigate(route = DogScreen.DogQueryScreen.name)}) {
                }
                Text(text = "DogQ")
        }


    }
}}



