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
import com.example.dogday.viewmodel.KennelDetailViewModel

@Composable
fun KennelDetailScreen(kennelId: String?) {
    val viewModel: KennelDetailViewModel = viewModel()

    // Fetch the kennel details using the kennelId
    LaunchedEffect(kennelId) {
        if (kennelId != null) {
            viewModel.fetchKennelById(kennelId)
        }
    }

    val kennel by viewModel.kennel.collectAsStateWithLifecycle()

    if (kennel != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = kennel?.imageUrl,
                contentDescription = kennel?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = kennel?.name ?: "", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Address: ${kennel?.address}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Contact: ${kennel?.contactInfo}",
                style = MaterialTheme.typography.bodyMedium
            )
            // Add more details as needed
        }
    } else {
        Text(text = "Kennel not found", modifier = Modifier.padding(16.dp))
    }
}
