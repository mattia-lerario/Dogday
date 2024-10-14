package com.example.dogday.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        Row {
            Column(modifier = Modifier.padding(5.dp)) {
                FloatingActionButton(onClick = {navController.navigate(route = DogScreen.AddDog.name)}) {
                }
                Text(text = "Legg til hund")
            }

            Column(modifier = Modifier.padding(5.dp)) {
                FloatingActionButton(onClick = {navController.navigate(route = DogScreen.Login.name)}) {
                }
                Text(text = "Login")
            }
        }


    }
}





@Preview
@Composable
fun PreviewDog(){
    SettingsScreen(navController = rememberNavController())
}