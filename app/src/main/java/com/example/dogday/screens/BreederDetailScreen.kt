package com.example.dogday.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dogday.viewmodel.BreederDetailViewModel
private const val API_KEY = "AIzaSyC6Krt10uCwyajM12ZMC9e8yUIdnTo6whY"
@Composable
fun BreederDetailScreen(breederId: String?) {
    val viewModel: BreederDetailViewModel = viewModel()

    LaunchedEffect(breederId) {
        if (breederId != null) {
            viewModel.fetchBreederById(breederId)
        }
    }

    val breeder by viewModel.breeder.collectAsStateWithLifecycle()

    if (breeder != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            breeder?.photoReference?.let { photoReference ->
                AsyncImage(
                    model = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=$photoReference&key=$API_KEY",
                    contentDescription = breeder?.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = breeder?.name ?: "", style = MaterialTheme.typography.titleLarge)
            DetailItem(label = "Address", value = breeder?.address)
            DetailItem(label = "Breeds", value = breeder?.dogBreeds?.joinToString(", "))
            DetailItem(label = "Business Status", value = breeder?.businessStatus)
            DetailItem(label = "Currently Open", value = breeder?.openingHours?.let { if (it) "Yes" else "No" })
            DetailItem(label = "Rating", value = breeder?.rating?.toString())
            DetailItem(label = "User Ratings", value = breeder?.userRatingsTotal?.toString())
        }
    } else {
        Text(text = "Breeder not found", modifier = Modifier.padding(16.dp))
    }
}


@Composable
fun DetailItem(label: String, value: String?) {
    if (!value.isNullOrEmpty()) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}