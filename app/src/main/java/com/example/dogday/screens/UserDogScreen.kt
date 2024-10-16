package com.example.dogday.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun UserDogScreen(navController: NavHostController){

    Column {
        DogsList(navController = navController)
    }



}