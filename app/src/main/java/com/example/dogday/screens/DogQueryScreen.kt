
package com.example.dogday.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogday.R
import com.example.dogday.ui.theme.ButtonColorLight

@Composable
fun DogQueryScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )

    {
        Image(
            painter = painterResource(R.drawable.dogday_logo),
            contentDescription = "DogDay Logo",
            modifier = Modifier.size(350.dp),
            contentScale = ContentScale.Fit
        )

        Text("Do you have a dog?", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // "Yes" Button
        Button(
            onClick = {
                navController.navigate(route = DogScreen.AddDog.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorLight
            ),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            Text("Yes", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "No" Button
        Button(
            onClick = {
                navController.navigate(route = DogScreen.Quiz.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonColorLight
            ),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            Text("No", style = MaterialTheme.typography.bodyLarge)
        }

        Image(
            painter = painterResource(R.drawable.dog_cartoon),
            contentDescription = "Dog Image",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )

    }
}
