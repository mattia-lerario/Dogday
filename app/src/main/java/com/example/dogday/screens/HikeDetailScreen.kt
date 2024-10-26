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
import com.example.dogday.viewmodel.HikeDetailViewModel

@Composable
fun HikeDetailScreen(hikeId: String?) {
    val viewModel: HikeDetailViewModel = viewModel()

    // Fetch the hike details using the hikeId
    LaunchedEffect(hikeId) {
        if (hikeId != null) {
            viewModel.fetchHikeById(hikeId)
        }
    }

    val hike by viewModel.hike.collectAsStateWithLifecycle()

    if (hike != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = hike?.imageUrl,
                contentDescription = hike?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = hike?.name ?: "", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Description: ${hike?.description}",
                style = MaterialTheme.typography.bodyMedium
            )
            // Add more details as needed
        }
    } else {
        Text(text = "Hike not found", modifier = Modifier.padding(16.dp))
    }
}
