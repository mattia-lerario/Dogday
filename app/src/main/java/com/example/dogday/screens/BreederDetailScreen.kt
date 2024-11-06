package com.example.dogday.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dogday.viewmodel.BreederDetailViewModel

@Composable
fun BreederDetailScreen(breederId: String?) {
    val viewModel: BreederDetailViewModel = viewModel()

    // Fetch the breeder details using the breederId
    LaunchedEffect(breederId) {
        if (breederId != null) {
            viewModel.fetchBreederById(breederId)
        }
    }

    val breeder by viewModel.breeder.collectAsStateWithLifecycle()

    if (breeder != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = breeder?.imageUrl,
                contentDescription = breeder?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = breeder?.name ?: "", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Address: ${breeder?.address}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Contact: ${breeder?.contactInfo}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Owner: ${breeder?.ownerName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Breeds: ${breeder?.dogBreeds?.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Description: ${breeder?.description}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        Text(text = "Breeder not found", modifier = Modifier.padding(16.dp))
    }
}
